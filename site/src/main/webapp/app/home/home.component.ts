import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { EntityArrayResponseType } from 'app/entities/category/service/category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  mostPopularSubCategories?: ISubCategory[];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private subCategoryService: SubCategoryService,
    private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.loadMostPopularSubCategories();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadMostPopularSubCategories(): void {
    const queryMostPopularSubCategoriesObject: any = {
      page: 0,
      size: 9,
      eargeload: true,
      sort: ["accessCount,desc"]
    }

    this.subCategoryService.query(queryMostPopularSubCategoriesObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  private onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.mostPopularSubCategories = dataFromBody;
  }

  private fillComponentAttributesFromResponseBody(data: ISubCategory[] | null): ISubCategory[] {
    return data ?? [];
  }

}
