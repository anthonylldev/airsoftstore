import { ISubCategory } from "../sub-category/sub-category.model";
import { ICategory } from "./category.model";

export interface ICategoryFilter extends  ICategory {
  subCategories?: ISubCategory[];
}
