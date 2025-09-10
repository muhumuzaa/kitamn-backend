ALTER TABLE dbo.user_profile
ADD line_1 NVARCHAR(100) NULL,
    line_2 NVARCHAR(100) NULL,
    city NVARCHAR(100) NULL,
    postal_code NVARCHAR(50) NULL,
    state_region NVARCHAR(100) NULL,
    country NVARCHAR(100) NOT NULL,

IF OBJECT_ID('dbo.UX_address_user_primary', 'U') IS NOT NULL
DROP INDEX UX_address_user_primary ON dbo.address;

IF OBJECT_ID('dbo.IX_address_user', 'U') IS NOT NULL
DROP INDEX IX_address_user ON dbo.address;

IF OBJECT_ID('dbo.address', 'U') IS NOT NULL
DROP TABLE dbo.address;