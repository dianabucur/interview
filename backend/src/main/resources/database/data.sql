-- Provide SQL scripts here
INSERT INTO ACCOUNT_USERS (USERNAME, PASSWORD, EMAIL, PHONE_NUMBER, NAME)
VALUES ('diana', '$2a$10$9T2zkXlUmkXizFiXyFhdBeM5PIEQIoeLvhCnLtnTw.6qVKhNqoqT2', 'diana@email.com', '0855368922', 'Diana Bucur-Dranca'),
       ('noah', '$2a$10$ybz1RIMioXao0GJ4tYXBSORw94Au6gLj6zLcZbiR39s8So0HNb7bi', 'noah@email.com', '0765370788', 'Noah Shane'),
       ('ella', '$2a$10$3rQ.D0OTb36nBd5M6.y9r.GOgjHbqn9RAHhhXq7.9.xIisnZrhp7O', 'ella@email.com', '0544313312', 'Ella Eyre'),
       ('david', '$2a$10$FG.PTcMNxOJQgzf4pwlVuOq.QcM7g7cD.7lje.g8peYd/k9.ZkDy6', 'david@email.com', '0657865578', 'David Stan'),
       ('mary', '$2a$10$33942exvkZOn.ZNiv0j0Y.0z2CmZVBFPZFw7DFhryfy6Z3wMuKonW', 'mary@email.com', '0765909341', 'Mary Anne');

INSERT INTO TRAILS (NAME, LOCATION, DISTANCE_KM, ELEVATION_GAIN_METERS, DIFFICULTY, DESCRIPTION) VALUES
('Eagle Rock Loop', 'Arkansas, USA', 42.0, 1500, 'ADVANCED', 'A scenic multi-day loop trail through Ouachita National Forest with river crossings and ridge views.'),
('Lake Agnes Teahouse Trail', 'Alberta, Canada', 7.0, 400, 'MODERATE', 'Popular trail in Banff National Park that ends at a charming alpine teahouse beside a lake.'),
('Tongariro Alpine Crossing', 'North Island, New Zealand', 19.4, 800, 'CHALLENGING', 'Crosses volcanic terrain with views of Mount Doom; considered one of the world’s best day hikes.'),
('Laugavegur Trail', 'Iceland', 55.0, 1200, 'ADVANCED', 'Multi-day trek through geothermal landscapes, glaciers, and colorful rhyolite mountains.'),
('The Narrows', 'Zion National Park, USA', 15.0, 200, 'MODERATE', 'Hike through a river in a slot canyon with towering walls on either side.'),
('Cinque Terre Coastal Trail', 'Liguria, Italy', 12.0, 550, 'EASY', 'Famous seaside trail connecting five picturesque Italian villages on the Mediterranean.'),
('Mount Takao Trail', 'Tokyo, Japan', 6.0, 400, 'EASY', 'A forested mountain trail just outside Tokyo, popular for day hikers and offering panoramic views.'),
('Preikestolen (Pulpit Rock)', 'Rogaland, Norway', 8.0, 500, 'CHALLENGING', 'A cliff-top hike to a 600-meter-high rock plateau overlooking Lysefjord.'),
('Kalalau Trail', 'Kauai, Hawaii, USA', 35.0, 1500, 'EXPERT', 'A rugged coastal trail on the Nā Pali Coast offering dramatic sea cliffs and secluded beaches.'),
('Plitvice Lakes Loop', 'Plitvice, Croatia', 10.0, 200, 'EASY', 'Walk among turquoise lakes and cascading waterfalls in a UNESCO World Heritage site.');

INSERT INTO HIKES (DATE, DURATION_HOURS, WEATHER, NOTES, USER_ID, TRAIL_ID, IS_PUBLIC, EXTERNAL_ID, VERSION) VALUES
('2024-06-15', 5.5, 'Sunny', 'Great weather, moderate traffic on the trail.', 1, 1, true, 'e6d7c4ce-9f82-4ef2-b10c-2e3a4b896e20', 0),
('2024-06-20', 3.0, 'Cloudy', 'Peaceful hike, saw deer and wildflowers.', 2, 2, false, '0d31dc61-235e-4ce6-a88f-8d2adf42d530', 0),
('2024-07-01', 7.2, 'Rainy', 'Slippery rocks, but the waterfalls were amazing.', 1, 3, false, '90d9cd06-5420-4697-b9f3-114acbd0c0bb', 0),
('2024-07-10', 6.5, 'Windy', 'Strong winds at the summit, but incredible views.', 3, 4, true, '3f247a7a-88b6-4709-8de7-c7cbd405cf63', 0),
('2024-07-14', 2.5, 'Partly Cloudy', 'Family-friendly trail with a nice teahouse at the top.', 2, 2, true, 'a0c90b3f-8fd7-4c2d-9425-bc80390ccaf6', 0),
('2024-07-18', 10.0, 'Clear', 'First solo hike. Peaceful, and well-marked trail.', 4, 5, false, '74bdde95-9267-4741-bf99-b51461784d0c', 0),
('2024-07-20', 4.0, 'Hot', 'Brought extra water. Beautiful coastal views.', 3, 6, true, 'f5d3c10c-e55d-49b4-a25e-d3e29e6c6d38', 0),
('2024-07-22', 3.8, 'Overcast', 'Crowded trail, but still enjoyable.', 1, 1, true, '37892ae7-78e3-47fa-95c5-bc3c9627e4a2', 0),
('2024-07-25', 5.0, 'Foggy', 'Low visibility, but cool atmosphere through the trees.', 2, 7, false, '32d64b88-b8b8-4a6e-978d-c9dc19e10749', 0),
('2024-07-28', 8.0, 'Sunny', 'Perfect hike. Crystal-clear skies and great photos.', 4, 9, true, '1ef3d60f-14a5-43a2-83a6-f44e2d5c5914', 0);