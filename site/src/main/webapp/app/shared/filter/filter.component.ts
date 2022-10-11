import { Component, Input, OnInit } from '@angular/core';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { EntityArrayResponseType } from 'app/entities/sub-category/service/sub-category.service';
import { IFilterOptions } from './filter.model';

@Component({
  selector: 'jhi-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {
  @Input() filters!: IFilterOptions;
  categories?: ICategory[];

  constructor(
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  clearAllFilters(): void {
    this.filters.clear();
  }

  clearFilter(filterName: string, value: string): void {
    this.filters.removeFilter(filterName, value);
  }

  private loadCategories(): void {
    const queryCategoriesObject: any = {
      sort: ['title,asc'],
    };

    this.categoryService.query(queryCategoriesObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onCategoryResponseSuccess(res);
      },
    });
  }

  private onCategoryResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillCategoryFromResponseBody(response.body);
    this.categories = dataFromBody;
  }

  private fillCategoryFromResponseBody(data: ICategory[] | null): ICategory[] {
    return data ?? [];
  }
}
