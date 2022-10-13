import { ISubCategory } from 'app/entities/sub-category/sub-category.model';

export interface ICategory {
  id: number;
  title?: string | null;
  cover?: string | null;
  coverContentType?: string | null;
  subCategories?: ISubCategory[];
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
