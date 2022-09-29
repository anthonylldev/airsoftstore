import { ISubCategory, NewSubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 68785,
  title: 'payment',
};

export const sampleWithPartialData: ISubCategory = {
  id: 60170,
  title: 'methodologies',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithFullData: ISubCategory = {
  id: 94018,
  title: 'Joyería invoice Granito',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithNewData: NewSubCategory = {
  title: 'Pescado Investment Bacon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
