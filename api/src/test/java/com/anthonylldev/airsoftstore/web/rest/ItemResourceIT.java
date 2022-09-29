package com.anthonylldev.airsoftstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.anthonylldev.airsoftstore.IntegrationTest;
import com.anthonylldev.airsoftstore.domain.Brand;
import com.anthonylldev.airsoftstore.domain.Item;
import com.anthonylldev.airsoftstore.domain.SubCategory;
import com.anthonylldev.airsoftstore.repository.ItemRepository;
import com.anthonylldev.airsoftstore.service.ItemService;
import com.anthonylldev.airsoftstore.service.criteria.ItemCriteria;
import com.anthonylldev.airsoftstore.service.dto.ItemDTO;
import com.anthonylldev.airsoftstore.service.mapper.ItemMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE = 0L;
    private static final Long UPDATED_PRICE = 1L;
    private static final Long SMALLER_PRICE = 0L - 1L;

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;
    private static final Integer SMALLER_STOCK = 0 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DETAILS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COVER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COVER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COVER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COVER_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Autowired
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .title(DEFAULT_TITLE)
            .price(DEFAULT_PRICE)
            .stock(DEFAULT_STOCK)
            .description(DEFAULT_DESCRIPTION)
            .productDetails(DEFAULT_PRODUCT_DETAILS)
            .cover(DEFAULT_COVER)
            .coverContentType(DEFAULT_COVER_CONTENT_TYPE);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        item.setBrand(brand);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        item.setSubCategory(subCategory);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .productDetails(UPDATED_PRODUCT_DETAILS)
            .cover(UPDATED_COVER)
            .coverContentType(UPDATED_COVER_CONTENT_TYPE);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createUpdatedEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        item.setBrand(brand);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceIT.createUpdatedEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        item.setSubCategory(subCategory);
        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testItem.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getProductDetails()).isEqualTo(DEFAULT_PRODUCT_DETAILS);
        assertThat(testItem.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testItem.getCoverContentType()).isEqualTo(DEFAULT_COVER_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setTitle(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setPrice(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setStock(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productDetails").value(hasItem(DEFAULT_PRODUCT_DETAILS)))
            .andExpect(jsonPath("$.[*].coverContentType").value(hasItem(DEFAULT_COVER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.productDetails").value(DEFAULT_PRODUCT_DETAILS))
            .andExpect(jsonPath("$.coverContentType").value(DEFAULT_COVER_CONTENT_TYPE))
            .andExpect(jsonPath("$.cover").value(Base64Utils.encodeToString(DEFAULT_COVER)));
    }

    @Test
    @Transactional
    void getItemsByIdFiltering() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        Long id = item.getId();

        defaultItemShouldBeFound("id.equals=" + id);
        defaultItemShouldNotBeFound("id.notEquals=" + id);

        defaultItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultItemShouldNotBeFound("id.greaterThan=" + id);

        defaultItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where title equals to DEFAULT_TITLE
        defaultItemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the itemList where title equals to UPDATED_TITLE
        defaultItemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultItemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the itemList where title equals to UPDATED_TITLE
        defaultItemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where title is not null
        defaultItemShouldBeFound("title.specified=true");

        // Get all the itemList where title is null
        defaultItemShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByTitleContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where title contains DEFAULT_TITLE
        defaultItemShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the itemList where title contains UPDATED_TITLE
        defaultItemShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where title does not contain DEFAULT_TITLE
        defaultItemShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the itemList where title does not contain UPDATED_TITLE
        defaultItemShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price equals to DEFAULT_PRICE
        defaultItemShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the itemList where price equals to UPDATED_PRICE
        defaultItemShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultItemShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the itemList where price equals to UPDATED_PRICE
        defaultItemShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is not null
        defaultItemShouldBeFound("price.specified=true");

        // Get all the itemList where price is null
        defaultItemShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is greater than or equal to DEFAULT_PRICE
        defaultItemShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the itemList where price is greater than or equal to UPDATED_PRICE
        defaultItemShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is less than or equal to DEFAULT_PRICE
        defaultItemShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the itemList where price is less than or equal to SMALLER_PRICE
        defaultItemShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is less than DEFAULT_PRICE
        defaultItemShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the itemList where price is less than UPDATED_PRICE
        defaultItemShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where price is greater than DEFAULT_PRICE
        defaultItemShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the itemList where price is greater than SMALLER_PRICE
        defaultItemShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock equals to DEFAULT_STOCK
        defaultItemShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the itemList where stock equals to UPDATED_STOCK
        defaultItemShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultItemShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the itemList where stock equals to UPDATED_STOCK
        defaultItemShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock is not null
        defaultItemShouldBeFound("stock.specified=true");

        // Get all the itemList where stock is null
        defaultItemShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock is greater than or equal to DEFAULT_STOCK
        defaultItemShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the itemList where stock is greater than or equal to UPDATED_STOCK
        defaultItemShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock is less than or equal to DEFAULT_STOCK
        defaultItemShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the itemList where stock is less than or equal to SMALLER_STOCK
        defaultItemShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock is less than DEFAULT_STOCK
        defaultItemShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the itemList where stock is less than UPDATED_STOCK
        defaultItemShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where stock is greater than DEFAULT_STOCK
        defaultItemShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the itemList where stock is greater than SMALLER_STOCK
        defaultItemShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description equals to DEFAULT_DESCRIPTION
        defaultItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the itemList where description equals to UPDATED_DESCRIPTION
        defaultItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the itemList where description equals to UPDATED_DESCRIPTION
        defaultItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description is not null
        defaultItemShouldBeFound("description.specified=true");

        // Get all the itemList where description is null
        defaultItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description contains DEFAULT_DESCRIPTION
        defaultItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the itemList where description contains UPDATED_DESCRIPTION
        defaultItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where description does not contain DEFAULT_DESCRIPTION
        defaultItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the itemList where description does not contain UPDATED_DESCRIPTION
        defaultItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllItemsByProductDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where productDetails equals to DEFAULT_PRODUCT_DETAILS
        defaultItemShouldBeFound("productDetails.equals=" + DEFAULT_PRODUCT_DETAILS);

        // Get all the itemList where productDetails equals to UPDATED_PRODUCT_DETAILS
        defaultItemShouldNotBeFound("productDetails.equals=" + UPDATED_PRODUCT_DETAILS);
    }

    @Test
    @Transactional
    void getAllItemsByProductDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where productDetails in DEFAULT_PRODUCT_DETAILS or UPDATED_PRODUCT_DETAILS
        defaultItemShouldBeFound("productDetails.in=" + DEFAULT_PRODUCT_DETAILS + "," + UPDATED_PRODUCT_DETAILS);

        // Get all the itemList where productDetails equals to UPDATED_PRODUCT_DETAILS
        defaultItemShouldNotBeFound("productDetails.in=" + UPDATED_PRODUCT_DETAILS);
    }

    @Test
    @Transactional
    void getAllItemsByProductDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where productDetails is not null
        defaultItemShouldBeFound("productDetails.specified=true");

        // Get all the itemList where productDetails is null
        defaultItemShouldNotBeFound("productDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByProductDetailsContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where productDetails contains DEFAULT_PRODUCT_DETAILS
        defaultItemShouldBeFound("productDetails.contains=" + DEFAULT_PRODUCT_DETAILS);

        // Get all the itemList where productDetails contains UPDATED_PRODUCT_DETAILS
        defaultItemShouldNotBeFound("productDetails.contains=" + UPDATED_PRODUCT_DETAILS);
    }

    @Test
    @Transactional
    void getAllItemsByProductDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList where productDetails does not contain DEFAULT_PRODUCT_DETAILS
        defaultItemShouldNotBeFound("productDetails.doesNotContain=" + DEFAULT_PRODUCT_DETAILS);

        // Get all the itemList where productDetails does not contain UPDATED_PRODUCT_DETAILS
        defaultItemShouldBeFound("productDetails.doesNotContain=" + UPDATED_PRODUCT_DETAILS);
    }

    @Test
    @Transactional
    void getAllItemsByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            brand = BrandResourceIT.createEntity(em);
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        item.setBrand(brand);
        itemRepository.saveAndFlush(item);
        Long brandId = brand.getId();

        // Get all the itemList where brand equals to brandId
        defaultItemShouldBeFound("brandId.equals=" + brandId);

        // Get all the itemList where brand equals to (brandId + 1)
        defaultItemShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    @Test
    @Transactional
    void getAllItemsBySubCategoryIsEqualToSomething() throws Exception {
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            subCategory = SubCategoryResourceIT.createEntity(em);
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        em.persist(subCategory);
        em.flush();
        item.setSubCategory(subCategory);
        itemRepository.saveAndFlush(item);
        Long subCategoryId = subCategory.getId();

        // Get all the itemList where subCategory equals to subCategoryId
        defaultItemShouldBeFound("subCategoryId.equals=" + subCategoryId);

        // Get all the itemList where subCategory equals to (subCategoryId + 1)
        defaultItemShouldNotBeFound("subCategoryId.equals=" + (subCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemShouldBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productDetails").value(hasItem(DEFAULT_PRODUCT_DETAILS)))
            .andExpect(jsonPath("$.[*].coverContentType").value(hasItem(DEFAULT_COVER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(Base64Utils.encodeToString(DEFAULT_COVER))));

        // Check, that the count call also returns 1
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemShouldNotBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .productDetails(UPDATED_PRODUCT_DETAILS)
            .cover(UPDATED_COVER)
            .coverContentType(UPDATED_COVER_CONTENT_TYPE);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testItem.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getProductDetails()).isEqualTo(UPDATED_PRODUCT_DETAILS);
        assertThat(testItem.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testItem.getCoverContentType()).isEqualTo(UPDATED_COVER_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.description(UPDATED_DESCRIPTION);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testItem.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getProductDetails()).isEqualTo(DEFAULT_PRODUCT_DETAILS);
        assertThat(testItem.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testItem.getCoverContentType()).isEqualTo(DEFAULT_COVER_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .productDetails(UPDATED_PRODUCT_DETAILS)
            .cover(UPDATED_COVER)
            .coverContentType(UPDATED_COVER_CONTENT_TYPE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testItem.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getProductDetails()).isEqualTo(UPDATED_PRODUCT_DETAILS);
        assertThat(testItem.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testItem.getCoverContentType()).isEqualTo(UPDATED_COVER_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
