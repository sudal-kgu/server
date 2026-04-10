INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(1, 1, 3, 10000,  0, 5,  1000, 3600, 'NONE', 0.0),
(1, 2, 3, 15000,  0, 7,  1200, 3600, 'NONE', 0.0),
(1, 3, 3, 22000,  0, 10, 1500, 3600, 'NONE', 0.0),
(1, 4, 3, 30000,  0, 15, 2000, 3600, 'NONE', 0.0);

INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(2, 1, 3, 10000,  0, 10, 2000, 7200, 'NONE', 0.0),
(2, 2, 3, 15000,  0, 15, 2500, 7200, 'NONE', 0.0),
(2, 3, 3, 22000,  0, 22, 3000, 7200, 'NONE', 0.0),
(2, 4, 3, 30000,  0, 30, 4000, 7200, 'NONE', 0.0);

INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(3, 1, 4, 30000, 10000, 30, 5000,  14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 2, 4, 40000, 13000, 45, 6000,  14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 3, 4, 55000, 17000, 65, 7500,  14400, 'QUIZ_REWARD_BOOST', 0.25),
(3, 4, 4, 70000, 22000, 90, 9000,  14400, 'QUIZ_REWARD_BOOST', 0.25);

INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(4, 1, 5, 100000, 50000, 50,  10000, 36000, 'ISLAND_BOOST', 0.10),
(4, 2, 5, 130000, 65000, 75,  12000, 36000, 'ISLAND_BOOST', 0.10),
(4, 3, 5, 170000, 80000, 110, 15000, 36000, 'ISLAND_BOOST', 0.10),
(4, 4, 5, 220000, 100000, 160, 20000, 36000, 'ISLAND_BOOST', 0.10);

INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(5, 1, 4, 10000, 0, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 200.0),
(5, 2, 4, 15000, 0, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 300.0),
(5, 3, 4, 22000, 0, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 400.0),
(5, 4, 4, 30000, 0, 0, 0, 0, 'DISPOSAL_REWARD_ADD', 500.0);

INSERT INTO building_spec (building_base_id, level, required_level, cost_shells, cost_gems, pph, required_fuel, duration_second, type, effect_value) VALUES
(6, 1, 5, 20000, 0, 0, 0, 0, 'QUIZ_REWARD_ADD', 500.0),
(6, 2, 5, 28000, 0, 0, 0, 0, 'QUIZ_REWARD_ADD', 600.0),
(6, 3, 5, 38000, 0, 0, 0, 0, 'QUIZ_REWARD_ADD', 700.0),
(6, 4, 5, 50000, 0, 0, 0, 0, 'QUIZ_REWARD_ADD', 800.0);