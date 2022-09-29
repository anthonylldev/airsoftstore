import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 2529,
  title: 'Namibia Salchichas',
};

export const sampleWithPartialData: ICategory = {
  id: 554,
  title: 'USB Reducido Director',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithFullData: ICategory = {
  id: 35731,
  title: 'virtual Silla navigating',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithNewData: NewCategory = {
  title: 'Public-key mano',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
