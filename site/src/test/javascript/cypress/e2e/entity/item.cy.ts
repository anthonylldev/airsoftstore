import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Item e2e test', () => {
  const itemPageUrl = '/item';
  const itemPageUrlPattern = new RegExp('/item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const itemSample = { title: 'invoice engineer', price: 58146, stock: 26431 };

  let item;
  let brand;
  let subCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/brands',
      body: { title: 'input Madera' },
    }).then(({ body }) => {
      brand = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sub-categories',
      body: { title: 'Intranet', cover: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=', coverContentType: 'unknown' },
    }).then(({ body }) => {
      subCategory = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/items/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/brands', {
      statusCode: 200,
      body: [brand],
    });

    cy.intercept('GET', '/api/sub-categories', {
      statusCode: 200,
      body: [subCategory],
    });
  });

  afterEach(() => {
    if (item) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/items/${item.id}`,
      }).then(() => {
        item = undefined;
      });
    }
  });

  afterEach(() => {
    if (brand) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/brands/${brand.id}`,
      }).then(() => {
        brand = undefined;
      });
    }
    if (subCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-categories/${subCategory.id}`,
      }).then(() => {
        subCategory = undefined;
      });
    }
  });

  it('Items menu should load Items page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Item').should('exist');
    cy.url().should('match', itemPageUrlPattern);
  });

  describe('Item page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(itemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Item page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/item/new$'));
        cy.getEntityCreateUpdateHeading('Item');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', itemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/items',
          body: {
            ...itemSample,
            brand: brand,
            subCategory: subCategory,
          },
        }).then(({ body }) => {
          item = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/items?page=0&size=20>; rel="last",<http://localhost/api/items?page=0&size=20>; rel="first"',
              },
              body: [item],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(itemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Item page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('item');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', itemPageUrlPattern);
      });

      it('edit button click should load edit Item page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Item');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', itemPageUrlPattern);
      });

      it.skip('edit button click should load edit Item page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Item');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', itemPageUrlPattern);
      });

      it('last delete button click should delete instance of Item', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('item').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', itemPageUrlPattern);

        item = undefined;
      });
    });
  });

  describe('new Item page', () => {
    beforeEach(() => {
      cy.visit(`${itemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Item');
    });

    it('should create an instance of Item', () => {
      cy.get(`[data-cy="title"]`).type('program').should('have.value', 'program');

      cy.get(`[data-cy="price"]`).type('9782').should('have.value', '9782');

      cy.get(`[data-cy="stock"]`).type('75385').should('have.value', '75385');

      cy.get(`[data-cy="description"]`).type('Rincón primary Gris').should('have.value', 'Rincón primary Gris');

      cy.get(`[data-cy="productDetails"]`).type('Multi Soluciones program').should('have.value', 'Multi Soluciones program');

      cy.setFieldImageAsBytesOfEntity('cover', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="brand"]`).select(1);
      cy.get(`[data-cy="subCategory"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        item = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', itemPageUrlPattern);
    });
  });
});
