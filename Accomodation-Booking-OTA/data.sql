-- ----------------------------
-- Users
-- ----------------------------
INSERT INTO public.users (email, full_name, is_email_verified, is_phone_verified, password_hash, phone_number, provider, "role", status)
VALUES
('alice@example.com', 'Alice Johnson', true, true, 'hashedpassword1', '1234567890', 'GOOGLE', 'GUEST', 'ACTIVE'),
('bob@example.com', 'Bob Smith', true, false, 'hashedpassword2', '0987654321', 'FACEBOOK', 'HOST', 'ACTIVE'),
('carol@example.com', 'Carol White', false, false, 'hashedpassword3', NULL, NULL, 'ADMIN', 'ACTIVE');

-- ----------------------------
-- Properties
-- ----------------------------
-- ----------------------------
-- Properties
-- ----------------------------
INSERT INTO public.properties (city, country, district, lat, lng, postal_code, state, street_address, ward, description, "name", status, type)
VALUES
('New York', 'USA', 'Manhattan', 40.712776, -74.005974, '10001', 'NY', '123 Broadway St', 'Ward 1', 'Luxury apartment in Manhattan', 'Manhattan Luxury Suites', 'ACCEPTED', 'APARTMENT'),
('Paris', 'France', 'Le Marais', 48.856613, 2.352222, '75003', 'Île-de-France', '456 Rue de Rivoli', 'Ward 2', 'Cozy flat in Paris', 'Paris Cozy Flat', 'PENDING', 'HOTEL');


-- ----------------------------
-- Property Amenities
-- ----------------------------
INSERT INTO public.property_amenities ("name") VALUES
('Free WiFi'), ('Pool'), ('Gym'), ('Breakfast included');

-- ----------------------------
-- Property Facilities
-- ----------------------------
INSERT INTO public.property_facilities ("name") VALUES
('Parking'), ('Elevator'), ('Reception 24/7');

-- ----------------------------
-- Property Amenity Mappings
-- ----------------------------
INSERT INTO public.property_amenity_mappings (property_id, amenity_id) VALUES
(1, 1), (1, 2), (2, 1), (2, 4);

-- ----------------------------
-- Property Facility Mappings
-- ----------------------------
INSERT INTO public.property_facility_mappings (property_id, facility_id) VALUES
(1, 1), (1, 2), (2, 3);

-- ----------------------------
-- Room Types
-- ----------------------------
INSERT INTO public.room_types (bed_type, max_adults, max_children, max_guest, "name", size_m2, smoking_allowed, total_rooms, view_type, cancellation_policy_id, property_id)
VALUES
('King', 2, 1, 3, 'Deluxe King Room', 35, false, 10, 'City View', NULL, 1),
('Queen', 2, 0, 2, 'Standard Queen Room', 25, true, 5, 'Street View', NULL, 1),
('Single', 1, 0, 1, 'Economy Single Room', 15, false, 3, 'Courtyard View', NULL, 2);

-- ----------------------------
-- Room Amenities
-- ----------------------------
INSERT INTO public.room_amenities ("name") VALUES
('Air Conditioning'), ('Mini Bar'), ('TV');

-- ----------------------------
-- Room Facilities
-- ----------------------------
INSERT INTO public.room_facilities ("name") VALUES
('Balcony'), ('Bathtub');

-- ----------------------------
-- Room Type Amenities
-- ----------------------------
INSERT INTO public.room_types_amenities (room_type_id, amenity_id) VALUES
(1, 1), (1, 2), (2, 1), (3, 3);

-- ----------------------------
-- Room Type Facilities
-- ----------------------------
INSERT INTO public.room_types_facilities (room_type_id, facility_id) VALUES
(1, 1), (2, 2), (3, 1);

-- ----------------------------
-- Room Inventory
-- ----------------------------
INSERT INTO public.room_inventory ("date", room_type_id, available_rooms) VALUES
('2025-12-08', 1, 5),
('2025-12-08', 2, 3),
('2025-12-08', 3, 2),
('2025-12-09', 1, 4),
('2025-12-09', 2, 3);

-- ----------------------------
-- Perks
-- ----------------------------
INSERT INTO public.perks ("name", code) VALUES
('Free Breakfast', 'BREAKFAST'),
('Late Checkout', 'LATE_CHECKOUT');

-- ----------------------------
-- Rate Plans
-- ----------------------------
INSERT INTO public.rate_plans ("name", prepayment_type, price, cancellation_policy_id, room_type_id)
VALUES
('Non-Refundable', 'FULL_PAYMENT', 150.0, NULL, 1),
('Flexible', 'NONE', 180.0, NULL, 1),
('Standard', 'DEPOSIT', 120.0, NULL, 2);

-- ----------------------------
-- Rate Plan Perks
-- ----------------------------
INSERT INTO public.rate_plan_perks (rate_plan_id, perk_id) VALUES
(1, 1), (2, 1), (2, 2), (3, 2);

-- ----------------------------
-- Media
-- ----------------------------
INSERT INTO public.media (url, caption, property_id) VALUES
('https://example.com/property1.jpg', 'Front View', 1),
('https://example.com/property2.jpg', 'Living Room', 2);

-- ----------------------------
-- Room Type Media
-- ----------------------------
INSERT INTO public.room_type_media (url, room_type_id) VALUES
('https://example.com/room1.jpg', 1),
('https://example.com/room2.jpg', 2),
('https://example.com/room3.jpg', 3);




-- ----------------------------
-- Rate Plans for room_type_id = 3
-- ----------------------------
INSERT INTO public.rate_plans ("name", prepayment_type, price, cancellation_policy_id, room_type_id)
VALUES
('Saver', 'FULL_PAYMENT', 110.0, NULL, 3),   -- ID 4
('Premium Flex', 'NONE', 160.0, NULL, 3);    -- ID 5


-- ----------------------------
-- Rate Plan Perks for room_type_id = 3
-- ----------------------------
INSERT INTO public.rate_plan_perks (rate_plan_id, perk_id) VALUES
(4, 1),
(5, 1),
(5, 2);
