import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFormService } from './item-form.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';
import { SubCategoryService } from 'app/entities/sub-category/service/sub-category.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let brandService: BrandService;
  let subCategoryService: SubCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    brandService = TestBed.inject(BrandService);
    subCategoryService = TestBed.inject(SubCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Brand query and add missing value', () => {
      const item: IItem = { id: 456 };
      const brand: IBrand = { id: 51882 };
      item.brand = brand;

      const brandCollection: IBrand[] = [{ id: 84734 }];
      jest.spyOn(brandService, 'query').mockReturnValue(of(new HttpResponse({ body: brandCollection })));
      const additionalBrands = [brand];
      const expectedCollection: IBrand[] = [...additionalBrands, ...brandCollection];
      jest.spyOn(brandService, 'addBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(brandService.query).toHaveBeenCalled();
      expect(brandService.addBrandToCollectionIfMissing).toHaveBeenCalledWith(
        brandCollection,
        ...additionalBrands.map(expect.objectContaining)
      );
      expect(comp.brandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SubCategory query and add missing value', () => {
      const item: IItem = { id: 456 };
      const subCategory: ISubCategory = { id: 61322 };
      item.subCategory = subCategory;

      const subCategoryCollection: ISubCategory[] = [{ id: 877 }];
      jest.spyOn(subCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: subCategoryCollection })));
      const additionalSubCategories = [subCategory];
      const expectedCollection: ISubCategory[] = [...additionalSubCategories, ...subCategoryCollection];
      jest.spyOn(subCategoryService, 'addSubCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(subCategoryService.query).toHaveBeenCalled();
      expect(subCategoryService.addSubCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        subCategoryCollection,
        ...additionalSubCategories.map(expect.objectContaining)
      );
      expect(comp.subCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 456 };
      const brand: IBrand = { id: 59092 };
      item.brand = brand;
      const subCategory: ISubCategory = { id: 76217 };
      item.subCategory = subCategory;

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.brandsSharedCollection).toContain(brand);
      expect(comp.subCategoriesSharedCollection).toContain(subCategory);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBrand', () => {
      it('Should forward to brandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(brandService, 'compareBrand');
        comp.compareBrand(entity, entity2);
        expect(brandService.compareBrand).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSubCategory', () => {
      it('Should forward to subCategoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(subCategoryService, 'compareSubCategory');
        comp.compareSubCategory(entity, entity2);
        expect(subCategoryService.compareSubCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
