import { ICategory } from 'app/entities/category/category.model';

export interface ISubCategory {
  id: number;
  title?: string | null;
  cover?: string | null;
  coverContentType?: string | null;
  category?: Pick<ICategory, 'id' | 'title'> | null;
}

export type NewSubCategory = Omit<ISubCategory, 'id'> & { id: null };
