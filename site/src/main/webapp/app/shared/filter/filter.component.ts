import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder } from '@angular/forms';
import { IBrandFilter } from 'app/entities/brand/brand-filter.model';
import { BrandService } from 'app/entities/brand/service/brand.service';
import { ICategoryFilter } from 'app/entities/category/category-filter.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { FilterOption, IFilterOptions } from './filter.model';

@Component({
  selector: 'jhi-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss'],
})
export class FilterComponent implements OnInit {
  @Input() filters!: IFilterOptions;
  categories?: ICategoryFilter[];
  brands?: IBrandFilter[];

  constructor(
    private categoryService: CategoryService,
    private subCategoryService: SubCategoryService,
    private brandService: BrandService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadBrands();
  }

  clearAllFilters(): void {
    this.filters.clear();
  }

  brandFilterChange(brand: IBrandFilter): void {
    if (brand.selected) {
      this.addFilter('brandId.in', [String(brand.id)]);
    } else {
      this.clearFilter('brandId.in', String(brand.id));
    }
  }

  private addFilter(filterName: string, values: string[]): void {
    const filter = this.filters.getFilterByName(filterName);

    if (filter !== undefined) {
      filter.addValue(...values);
    } else {
      this.filters.addFilter('brandId.in', ...values);
    }
  }

  private clearFilter(filterName: string, value: string): void {
    this.filters.removeFilter(filterName, value);
  }

  private loadFilters(): void {
    const filter = this.filters.getFilterByName('brandId.in');

    if (filter !== undefined) {
      const values = filter.values;

      this.brands?.forEach(brand => {
        const res = values.find(valor => valor === String(brand.id));

        if (res !== undefined) {
          brand.selected = true;
        }
      });
    }
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

  private loadBrands(): void {
    const queryBrandObject: any = {
      sort: ['title,asc'],
    };

    this.brandService.query(queryBrandObject).subscribe({
      next: (res: HttpResponse<IBrandFilter[]>) => {
        this.onBrandResponseSuccess(res);
      },
    });
  }

  private onBrandResponseSuccess(response: HttpResponse<IBrandFilter[]>): void {
    const dataFromBody = this.fillBrandFromResponseBody(response.body);
    this.brands = dataFromBody;
    this.loadFilters();
  }

  private fillBrandFromResponseBody(data: IBrandFilter[] | null): IBrandFilter[] {
    return data ?? [];
  }
}
