import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'airsoftstoreApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'sub-category',
        data: { pageTitle: 'airsoftstoreApp.subCategory.home.title' },
        loadChildren: () => import('./sub-category/sub-category.module').then(m => m.SubCategoryModule),
      },
      {
        path: 'brand',
        data: { pageTitle: 'airsoftstoreApp.brand.home.title' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'airsoftstoreApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
