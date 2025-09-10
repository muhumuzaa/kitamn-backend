-- 1) Add address fields to user_profile if they don't exist
IF NOT EXISTS (
    SELECT 1 FROM sys.columns
    WHERE Name = N'line_1' AND object_id = OBJECT_ID(N'dbo.user_profile')
)
BEGIN
ALTER TABLE dbo.user_profile ADD
    line_1       NVARCHAR(100) NULL,
        line_2       NVARCHAR(100) NULL,
        city         NVARCHAR(100) NULL,
        state_region NVARCHAR(100) NULL,
        postal_code  NVARCHAR(50)  NULL,
        country      NVARCHAR(100) NULL,
        label        NVARCHAR(50)  NULL;
END
GO

-- 2) Copy data from address into user_profile
--    Rule: take the primary address if it exists, else the most recent one.
IF OBJECT_ID(N'dbo.address', N'U') IS NOT NULL
BEGIN
    ;WITH pick AS (
    SELECT a.*
    FROM dbo.address a
    WHERE a.is_primary = 1
    UNION ALL
    SELECT a2.*
    FROM dbo.address a2
    WHERE a2.is_primary = 0
      AND NOT EXISTS (
        SELECT 1 FROM dbo.address b
        WHERE b.user_id = a2.user_id AND b.is_primary = 1
    )
)
UPDATE up
SET
    up.line_1       = p.line_1,
    up.line_2       = p.line_2,
    up.city         = p.city,
    up.state_region = p.state_region,
    up.postal_code  = p.postal_code,
    up.country      = p.country,
    up.label        = p.label
    FROM dbo.user_profile up
    JOIN pick p
ON p.user_id = up.user_id;
END
GO

-- 3) Drop indexes on address that may block dropping the table (if they exist)
IF OBJECT_ID(N'dbo.address', N'U') IS NOT NULL
BEGIN
    IF EXISTS (
        SELECT 1
        FROM sys.indexes
        WHERE name = N'UX_address_user_primary'
          AND object_id = OBJECT_ID(N'dbo.address')
    )
BEGIN
DROP INDEX UX_address_user_primary ON dbo.address;
END;

    IF EXISTS (
        SELECT 1
        FROM sys.indexes
        WHERE name = N'IX_address_user'
          AND object_id = OBJECT_ID(N'dbo.address')
    )
BEGIN
DROP INDEX IX_address_user ON dbo.address;
END;
END
GO

-- 4) Drop address table
IF OBJECT_ID(N'dbo.address', N'U') IS NOT NULL
BEGIN
DROP TABLE dbo.address;
END
GO
