import { IBrand, NewBrand } from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 69581,
  title: 'Humano copying experiences',
};

export const sampleWithPartialData: IBrand = {
  id: 68630,
  title: 'mobile Pizza',
};

export const sampleWithFullData: IBrand = {
  id: 53595,
  title: 'Metal Sol empower',
};

export const sampleWithNewData: NewBrand = {
  title: 'primary',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
