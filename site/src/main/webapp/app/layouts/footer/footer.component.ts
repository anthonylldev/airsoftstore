import { Component, OnInit } from '@angular/core';
import { EntityArrayResponseType, SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ["./footer.component.scss"]
})
export class FooterComponent implements OnInit {
  mostPopularSubCategories?: ISubCategory[];
  email = "";

  constructor(
    private subCategoryService: SubCategoryService
  ) {}

  ngOnInit(): void {
    this.loadMostPopularSubCategories();
  }

  subscribe(): void {
    // TODO subscribe to newsletter
  }

  private loadMostPopularSubCategories(): void {
    const queryMostPopularSubCategoriesObject: any = {
      page: 0,
      size: 3,
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
}
