import { IBrand } from "./brand.model";

export interface IBrandFilter extends IBrand {
  selected?: boolean;
}

