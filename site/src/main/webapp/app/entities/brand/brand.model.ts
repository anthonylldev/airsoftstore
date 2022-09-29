export interface IBrand {
  id: number;
  title?: string | null;
}

export type NewBrand = Omit<IBrand, 'id'> & { id: null };
