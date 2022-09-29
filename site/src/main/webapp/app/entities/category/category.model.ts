export interface ICategory {
  id: number;
  title?: string | null;
  cover?: string | null;
  coverContentType?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
