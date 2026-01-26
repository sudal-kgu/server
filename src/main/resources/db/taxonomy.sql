-- [1] 플라스틱 용기류
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '플라스틱 용기류'
  AND s.alias IN ('Bottle', 'Container', 'Cosmetics', 'Drinking straw', 'Jug', 'Pitcher (Container)', 'Soap dispenser', 'Syringe');

-- [2] 비닐류
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '비닐류'
  AND s.alias IN ('Adhesive tape', 'Balloon', 'Plastic bag');

-- [3] 스티로폼
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '스티로폼' AND s.alias IN ('Box');

-- [4] 금속캔
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '금속캔'
  AND s.alias IN ('Tin can', 'Beer', 'Cocktail shaker', 'Cooking spray', 'Hair spray');

-- [5] 고철
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '고철'
  AND s.alias IN ('Axe', 'Chisel', 'Hammer', 'Kettle', 'Knife', 'Ladle', 'Nail (Construction)', 'Padlock', 'Ratchet (Device)', 'Scissors', 'Screwdriver', 'Spatula', 'Spoon', 'Sword', 'Tool', 'Waffle iron', 'Wok', 'Wrench', 'Auto part', 'Coin', 'Dumbbell', 'Fork');

-- [6] 재질별분리
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '재질별분리' AND s.alias IN ('Umbrella', 'Tent', 'Baby transport', 'Stretcher');

-- [7] 종이류
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '종이류'
  AND s.alias IN ('Book', 'Envelope', 'Poster', 'Whiteboard', 'Paper cutter', 'Paper towel', 'Filing cabinet', 'Magazine', 'Newspaper');

-- [8] 종이팩
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '종이팩' AND s.alias IN ('Milk', 'Juice');

-- [9] 유리병류
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '유리병류'
  AND s.alias IN ('Bottle', 'Jar', 'Beaker', 'Wine glass', 'Wine bottle', 'Beer bottle');

-- [10] 의류 및 원단류
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '의류 및 원단류'
  AND s.alias IN ('Backpack', 'Belt', 'Boot', 'Clothing', 'Coat', 'Dress', 'Glove', 'Handbag', 'Hat', 'High heels', 'Jacket', 'Jeans', 'Sandal', 'Scarf', 'Shirt', 'Shorts', 'Skirt', 'Sock', 'Suit', 'Suitcase', 'Swimwear', 'Tie', 'Trousers', 'Towel', 'Pillow', 'Curtain');

-- [11] 폐가전제품
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '폐가전제품'
  AND s.alias IN ('Alarm clock', 'Blender', 'Calculator', 'Camera', 'Cassette deck', 'Ceiling fan', 'Clock', 'Computer keyboard', 'Computer monitor', 'Computer mouse', 'Corded phone', 'Digital clock', 'Dishwasher', 'Fax', 'Flashlight', 'Food processor', 'Gas stove', 'Hair dryer', 'Heater', 'Humidifier', 'Ipod', 'Laptop', 'Microphone', 'Microwave oven', 'Mixer', 'Mobile phone', 'Musical keyboard', 'Oven', 'Printer', 'Refrigerator', 'Remote control', 'Sewing machine', 'Slow cooker', 'Tablet computer', 'Telephone', 'Television', 'Toaster', 'Waffle iron', 'Wall clock', 'Washing machine', 'Watch', 'Coffeemaker', 'Mechanical fan');

-- [12] 대형 폐기물
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '대형 폐기물'
  AND s.alias IN ('Accordion', 'Banjo', 'Billiard table', 'Bookcase', 'Cello', 'Chair', 'Chest of drawers', 'Closet', 'Couch', 'Cupboard', 'Desk', 'Drum', 'Flute', 'Guitar', 'Harp', 'Harpsichord', 'Infant bed', 'Jacuzzi', 'Kitchen & dining room table', 'Ladder', 'Loveseat', 'Musical instrument', 'Nightstand', 'Oboe', 'Organ (Musical Instrument)', 'Piano', 'Picnic basket', 'Saxophone', 'Shelf', 'Sofa bed', 'Stool', 'Studio couch', 'Table', 'Trombone', 'Trumpet', 'Violin', 'Wardrobe', 'Bed', 'Bench', 'Bicycle', 'Bicycle wheel', 'Wheelchair');

-- [13] 음식물 쓰레기
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '음식물 쓰레기'
  AND s.alias IN ('Apple', 'Artichoke', 'Bagel', 'Baked goods', 'Banana', 'Bell pepper', 'Bread', 'Broccoli', 'Burrito', 'Cabbage', 'Cake', 'Candy', 'Cantaloupe', 'Carrot', 'Cheese', 'Coconut', 'Cookie', 'Croissant', 'Cucumber', 'Dairy Product', 'Dessert', 'Donut', 'Egg (Food)', 'Fast food', 'Food', 'French fries', 'Fruit', 'Grape', 'Grapefruit', 'Guacamole', 'Hamburger', 'Hot dog', 'Ice cream', 'Lemon', 'Mango', 'Muffin', 'Mushroom', 'Orange', 'Pancake', 'Pasta', 'Peach', 'Pear', 'Pizza', 'Pomegranate', 'Popcorn', 'Potato', 'Pretzel', 'Pumpkin', 'Salad', 'Sandwich', 'Seafood', 'Snack', 'Strawberry', 'Submarine sandwich', 'Sushi', 'Taco', 'Tart', 'Tomato', 'Vegetable', 'Waffle', 'Watermelon', 'Winter melon', 'Zucchini');

-- [14] 불연성 종량제
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '불연성 종량제'
  AND s.alias IN ('Mirror', 'Bowl', 'Plate', 'Platter', 'Saucer', 'Tableware', 'Vase', 'Flowerpot');

-- [15] 전용함
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '전용함' AND s.alias IN ('Light bulb', 'Battery');

-- [16] 주의
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '주의'
  AND s.alias IN ('Bomb', 'Missile', 'Weapon', 'Syringe');

-- [17] 전문시설
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '전문시설'
  AND s.alias IN ('Tire', 'Ambulance', 'Barge', 'Boat', 'Bus', 'Car', 'Canoe', 'Cart', 'Fire engine', 'Gondola', 'Helicopter', 'Jet ski', 'Limousine', 'Motorcycle', 'Segway', 'Snowmobile', 'Submarine', 'Tank', 'Taxi', 'Train', 'Truck', 'Van', 'Vehicle');

-- [18] 종량제봉투
INSERT INTO TRASH_TAXONOMY (category_id, subcategory_id)
SELECT c.category_id, s.subcategory_id FROM TRASH_CATEGORY c, TRASH_SUB_CATEGORY s
WHERE c.name = '종량제봉투'
  AND s.alias IN ('Diaper', 'Band-aid', 'Toothbrush', 'Eraser', 'Pen');