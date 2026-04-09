INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(1, 1, 5, 1000, 3600, 'NONE', 0.0),
(1, 2, 7, 1200, 3600, 'NONE', 0.0),
(1, 3, 10, 1500, 3600, 'NONE', 0.0),
(1, 4, 15, 2000, 3600, 'NONE', 0.0);

INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(2, 1, 10, 2000, 7200, 'NONE', 0.0),
(2, 2, 15, 2500, 7200, 'NONE', 0.0),
(2, 3, 22, 3000, 7200, 'NONE', 0.0),
(2, 4, 30, 4000, 7200, 'NONE', 0.0);

INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(3, 1, 30, 5000, 14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 2, 45, 6000, 14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 3, 65, 7500, 14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 4, 90, 9000, 14400, 'QUIZ_REWARD_BOOST', 0.25);

INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(4, 1, 50, 10000, 36000, 'ISLAND_BOOST', 0.10),
(4, 2, 75, 12000, 36000, 'ISLAND_BOOST', 0.10),
(4, 3, 110, 15000, 36000, 'ISLAND_BOOST', 0.10),
(4, 4, 160, 20000, 36000, 'ISLAND_BOOST', 0.10);

INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(5, 1, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 200.0),
(5, 2, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 300.0),
(5, 3, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 400.0),
(5, 4, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 500.0);

INSERT INTO building_spec (building_base_id, level, pph, required_fuel, duration_second, type, effect_value) VALUES
(6, 1, 0, 0, 0, 'QUIZ_REWARD_ADD', 500.0),
(6, 2, 0, 0, 0, 'QUIZ_REWARD_ADD', 600.0),
(6, 3, 0, 0, 0, 'QUIZ_REWARD_ADD', 700.0),
(6, 4, 0, 0, 0, 'QUIZ_REWARD_ADD', 800.0);