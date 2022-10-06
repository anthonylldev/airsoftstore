import { IBrand } from 'app/entities/brand/brand.model';
import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

export interface IItem {
  id: number;
  title?: string | null;
  price?: number | null;
  stock?: number | null;
  description?: string | null;
  cover?: string | null;
  coverContentType?: string | null;
  brand?: Pick<IBrand, 'id' | 'title'> | null;
  subCategory?: Pick<ISubCategory, 'id' | 'title'> | null;
  inclusionDate?: Date | null;
}



export type NewItem = Omit<IItem, 'id'> & { id: null };
