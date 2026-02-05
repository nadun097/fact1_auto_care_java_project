-- Mock Vehicle Maintenance Data for Fact One Auto Care
-- Database: jobschedulesystem

USE jobschedulesystem;

-- Insert mock customer and vehicle maintenance records
INSERT INTO jobtable (Customer_Name, Customer_Mail, Phone, VehicalNo, Times, Date) VALUES
('John Silva', 'john.silva@email.com', '0771234567', 'ABC-1234', 'Vehicle Full Service, Vehicle Inspection', '2026-01-15'),
('Mary Fernando', 'mary.fernando@email.com', '0772345678', 'XYZ-5678', 'Vehicle Normal Service, Vehicle body wash', '2026-01-18'),
('0772345678', 'david.perera@email.com', '0773456789', 'LMN-9012', 'Vehicle Running Repair, Vehicle Brakes Service', '2026-01-20'),
('Sarah Jayawardena', 'sarah.jay@email.com', '0774567890', 'PQR-3456', 'Vehicle Tire Rotation, Vehicle Inspection', '2026-01-22'),
('Michael De Silva', 'michael.ds@email.com', '0775678901', 'STU-7890', 'Vehicle Full Service, Vehicle Scan/Diagnose', '2026-01-25'),
('Anna Wickramasinghe', 'anna.wick@email.com', '0776789012', 'VWX-1122', 'Vehicle body wash', '2026-01-28'),
('Robert Gunasekara', 'robert.g@email.com', '0777890123', 'YZA-3344', 'Vehicle Normal Service, Vehicle Brakes Service, Vehicle Tire Rotation', '2026-02-01'),
('Linda Rajapaksa', 'linda.raja@email.com', '0778901234', 'BCD-5566', 'Vehicle Full Service, Vehicle Inspection, Vehicle Scan/Diagnose', '2026-02-03'),
('James Bandara', 'james.b@email.com', '0779012345', 'EFG-7788', 'Vehicle Running Repair', '2026-02-04'),
('Patricia Mendis', 'patricia.m@email.com', '0770123456', 'HIJ-9900', 'Vehicle body wash, Vehicle Tire Rotation', '2026-02-05'),
('William Amarasinghe', 'william.a@email.com', '0771122334', 'KLM-1357', 'Vehicle Normal Service, Vehicle Inspection', '2026-01-16'),
('Elizabeth Dissanayake', 'elizabeth.d@email.com', 'elizabeth.d@email.com', 'NOP-2468', 'Vehicle Full Service, Vehicle Brakes Service', '2026-01-19'),
('Thomas Wijesinghe', 'thomas.w@email.com', '0773344556', 'QRS-3579', 'Vehicle Scan/Diagnose, Vehicle Inspection', '2026-01-21'),
('Jennifer Karunaratne', 'jennifer.k@email.com', '0774455667', 'TUV-4680', 'Vehicle body wash, Vehicle Normal Service', '2026-01-24'),
('Charles Seneviratne', 'charles.s@email.com', '0775566778', 'WXY-5791', 'Vehicle Running Repair, Vehicle Tire Rotation, Vehicle Brakes Service', '2026-01-27'),
('Barbara Gunawardena', 'barbara.g@email.com', '0776677889', 'ZAB-6802', 'Vehicle Full Service, Vehicle body wash', '2026-01-30'),
('Joseph Fonseka', 'joseph.f@email.com', '0777788990', 'CDE-7913', 'Vehicle Inspection, Vehicle Scan/Diagnose', '2026-02-02'),
('Susan Rodrigo', 'susan.r@email.com', '0778899001', 'FGH-8024', 'Vehicle Normal Service, Vehicle Tire Rotation', '2026-02-04'),
('Daniel Cooray', 'daniel.c@email.com', '0779900112', 'IJK-9135', 'Vehicle Full Service, Vehicle Running Repair, Vehicle Inspection', '2026-02-05'),
('Nancy Senanayake', 'nancy.s@email.com', '0770011223', 'LMN-0246', 'Vehicle body wash, Vehicle Brakes Service', '2026-01-17');

-- Additional records for testing various service combinations
INSERT INTO jobtable (Customer_Name, Customer_Mail, Phone, VehicalNo, Times, Date) VALUES
('Mark Thompson', 'mark.t@email.com', '0771234000', 'TEST-001', 'Vehicle Full Service', '2026-01-10'),
('Emily Watson', 'emily.w@email.com', '0772345000', 'TEST-002', 'Vehicle Normal Service', '2026-01-11'),
('Alex Brown', 'alex.b@email.com', '0773456000', 'TEST-003', 'Vehicle body wash', '2026-01-12'),
('Sophie Davis', 'sophie.d@email.com', '0774567000', 'TEST-004', 'Vehicle Running Repair', '2026-01-13'),
('Oliver Martin', 'oliver.m@email.com', '0775678000', 'TEST-005', 'Vehicle Brakes Service', '2026-01-14'),
('Emma Garcia', 'emma.g@email.com', '0776789000', 'TEST-006', 'Vehicle Tire Rotation', '2026-01-15'),
('Noah Martinez', 'noah.m@email.com', '0777890000', 'TEST-007', 'Vehicle Inspection', '2026-01-16'),
('Ava Rodriguez', 'ava.r@email.com', '0778901000', 'TEST-008', 'Vehicle Scan/Diagnose', '2026-01-17'),
('Liam Wilson', 'liam.w@email.com', '0779012000', 'TEST-009', 'Vehicle Full Service, Vehicle body wash, Vehicle Inspection', '2026-01-18'),
('Mia Anderson', 'mia.a@email.com', '0770123000', 'TEST-010', 'Vehicle Normal Service, Vehicle Tire Rotation, Vehicle Scan/Diagnose', '2026-01-19');

-- Summary of inserted records
-- Total: 30 mock vehicle maintenance records
-- Date range: January 10, 2026 - February 5, 2026
-- Services covered: All 8 service types
-- Various combinations to test the billing system
