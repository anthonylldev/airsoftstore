import { Component, Input, OnInit } from '@angular/core';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { EntityArrayResponseType, SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { IFilterOptions } from './filter.model';

@Component({
  selector: 'jhi-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {
  @Input() filters!: IFilterOptions;
  categories?: ICategory[];
  brands?: IBrand[];

  constructor(
    private categoryService: CategoryService,
    private subCategoryService: SubCategoryService,
    private brandService: BrandService
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadBrands();
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

    this.categories.forEach(category => {
      this.loadSubCategories(category)
    });
  }

  private fillCategoryFromResponseBody(data: ICategory[] | null): ICategory[] {
    return data ?? [];
  }

  private loadSubCategories(category: ICategory): void {
    const querySubCategoriesObject: any = {
      sort: ["title,asc"],
      "categoryId.in": category.id
    };

    this.subCategoryService.query(querySubCategoriesObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onSubCategoryResponseSuccess(res, category);
      },
    });
  }

  private onSubCategoryResponseSuccess(response: EntityArrayResponseType, category: ICategory): void {
    const dataFromBody = this.fillSubCategoryFromResponseBody(response.body);
    category.subCategories = dataFromBody;

  }

  private fillSubCategoryFromResponseBody(data: ISubCategory[] | null): ISubCategory[] {
    return data ?? [];
  }

  private loadBrands(): void {
    const queryBrandObject: any = {
      sort: ["title,asc"]
    };

    this.brandService.query(queryBrandObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onBrandResponseSuccess(res);
      },
    });
  }

  private onBrandResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillBrandFromResponseBody(response.body);
    this.brands = dataFromBody;
  }

  private fillBrandFromResponseBody(data: IBrand[] | null): IBrand[] {
    return data ?? [];
  }
}
