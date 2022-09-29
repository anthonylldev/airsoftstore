import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 89800,
  title: 'Bedfordshire input',
  price: 45749,
  stock: 49177,
};

export const sampleWithPartialData: IItem = {
  id: 98428,
  title: 'Account technologies Checking',
  price: 64739,
  stock: 66899,
  description: 'basado next-generation Sabroso',
  productDetails: 'web-enabled Algodón',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithFullData: IItem = {
  id: 59519,
  title: 'Castilla-La Checking synthesize',
  price: 64608,
  stock: 96728,
  description: 'empower Oro Artesanal',
  productDetails: 'Navarra',
  cover: '../fake-data/blob/hipster.png',
  coverContentType: 'unknown',
};

export const sampleWithNewData: NewItem = {
  title: 'tolerancia Teclado',
  price: 12101,
  stock: 62380,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
