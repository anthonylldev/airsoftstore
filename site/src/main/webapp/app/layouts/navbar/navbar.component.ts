import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { ICategoryFilter } from 'app/entities/category/category-filter.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];

  categories?: ICategoryFilter[];
  subCategories?: ISubCategory[];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private categoryService: CategoryService,
    private subCategoryService: SubCategoryService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });

    this.loadCategories();
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  private loadCategories(): void {
    const queryCategoriesObject: any = {
      sort: ['title,asc'],
    };

    this.categoryService.query(queryCategoriesObject).subscribe({
      next: (res: HttpResponse<ICategoryFilter[]>) => {
        this.onCategoryResponseSuccess(res);
      },
    });
  }

  private onCategoryResponseSuccess(response: HttpResponse<ICategoryFilter[]>): void {
    const dataFromBody = this.fillCategoryFromResponseBody(response.body);
    this.categories = dataFromBody;

    this.categories.forEach(category => {
      this.loadSubCategories(category);
    });
  }

  private fillCategoryFromResponseBody(data: ICategoryFilter[] | null): ICategoryFilter[] {
    return data ?? [];
  }

  private loadSubCategories(category: ICategoryFilter): void {
    const querySubCategoriesObject: any = {
      sort: ['title,asc'],
      'categoryId.in': category.id,
    };

    this.subCategoryService.query(querySubCategoriesObject).subscribe({
      next: (res: HttpResponse<ISubCategory[]>) => {
        this.onSubCategoryResponseSuccess(res, category);
      },
    });
  }

  private onSubCategoryResponseSuccess(response: HttpResponse<ISubCategory[]>, category: ICategoryFilter): void {
    const dataFromBody = this.fillSubCategoryFromResponseBody(response.body);
    category.subCategories = dataFromBody;
  }

  private fillSubCategoryFromResponseBody(data: ISubCategory[] | null): ISubCategory[] {
    return data ?? [];
  }
}
