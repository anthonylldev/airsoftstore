import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { EntityArrayResponseType } from 'app/entities/category/service/category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IItem } from 'app/entities/item/item.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  mostPopularSubCategories?: ISubCategory[];
  itemsNews?: IItem[];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private subCategoryService: SubCategoryService,
    private itemService: ItemService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.loadMostPopularSubCategories();
    this.loadItemsNews();
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
      eagerload: true,
      sort: ['accessCount,desc'],
    };

    this.subCategoryService.query(queryMostPopularSubCategoriesObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onSubCategoryResponseSuccess(res);
      },
    });
  }

  private onSubCategoryResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillMostPopularSubCategoriesFromResponseBody(response.body);
    this.mostPopularSubCategories = dataFromBody;
  }

  private fillMostPopularSubCategoriesFromResponseBody(data: ISubCategory[] | null): ISubCategory[] {
    return data ?? [];
  }

  private loadItemsNews(): void {
    const queryItemsNewsObject: any = {
      page: 0,
      size: 26,
      eagerload: true,
      sort: ['inclusionDate,desc'],
    };

    this.itemService.query(queryItemsNewsObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onItemResponseSuccess(res);
      },
    });
  }

  private onItemResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillItemsNewsFromResponseBody(response.body);
    this.itemsNews = dataFromBody;
  }

  private fillItemsNewsFromResponseBody(data: IItem[] | null): IItem[] {
    return data ?? [];
  }
}
