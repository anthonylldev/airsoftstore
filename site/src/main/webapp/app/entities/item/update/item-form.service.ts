import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IItem, NewItem } from '../item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItem for edit and NewItemFormGroupInput for create.
 */
type ItemFormGroupInput = IItem | PartialWithRequiredKeyOf<NewItem>;

type ItemFormDefaults = Pick<NewItem, 'id'>;

type ItemFormGroupContent = {
  id: FormControl<IItem['id'] | NewItem['id']>;
  title: FormControl<IItem['title']>;
  price: FormControl<IItem['price']>;
  stock: FormControl<IItem['stock']>;
  description: FormControl<IItem['description']>;
  cover: FormControl<IItem['cover']>;
  coverContentType: FormControl<IItem['coverContentType']>;
  brand: FormControl<IItem['brand']>;
  subCategory: FormControl<IItem['subCategory']>;
  inclusionDate: FormControl<IItem['inclusionDate']>;
};

export type ItemFormGroup = FormGroup<ItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemFormService {
  createItemFormGroup(item: ItemFormGroupInput = { id: null }): ItemFormGroup {
    const itemRawValue = {
      ...this.getFormDefaults(),
      ...item,
    };
    return new FormGroup<ItemFormGroupContent>({
      id: new FormControl(
        { value: itemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(itemRawValue.title, {
        validators: [Validators.required],
      }),
      price: new FormControl(itemRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      stock: new FormControl(itemRawValue.stock, {
        validators: [Validators.required, Validators.min(0)],
      }),
      description: new FormControl(itemRawValue.description),
      cover: new FormControl(itemRawValue.cover),
      coverContentType: new FormControl(itemRawValue.coverContentType),
      brand: new FormControl(itemRawValue.brand, {
        validators: [Validators.required],
      }),
      subCategory: new FormControl(itemRawValue.subCategory, {
        validators: [Validators.required],
      }),
      inclusionDate: new FormControl({
        value: itemRawValue.inclusionDate ? itemRawValue.inclusionDate : this.createDateAsUTC(),
        disabled: true,
      }),
    });
  }

  getItem(form: ItemFormGroup): IItem | NewItem {
    return form.getRawValue() as IItem | NewItem;
  }

  resetForm(form: ItemFormGroup, item: ItemFormGroupInput): void {
    const itemRawValue = { ...this.getFormDefaults(), ...item };
    form.reset(
      {
        ...itemRawValue,
        id: { value: itemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ItemFormDefaults {
    return {
      id: null,
    };
  }

  private createDateAsUTC(): Date {
    const date: Date = new Date();

    return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds()));
  }
}
