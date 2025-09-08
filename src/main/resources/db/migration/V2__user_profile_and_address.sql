
-- table for user profile: has 1-1 with user_account table
CREATE TABLE dbo.user_profile(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    phone NVARCHAR(30) NULL,
    avatar_url NVARCHAR(500) NULL,
    bio NVARCHAR(500) NULL,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),

    CONSTRAINT FK_user_profile_user FOREIGN KEY(user_id) REFERENCES dbo.user_account(id) ON DELETE CASCADE,
    CONSTRAINT UQ_user_profile_user UNIQUE (user_id) --makes 1-1 relationship
);
GO


--table for addresses. has 1-many with user-account
CREATE TABLE dbo.address(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label NVARCHAR(50) NULL, --home, work
    line_1 NVARCHAR(100) NOT NULL,
    line_2 NVARCHAR(100) NULL,
    city NVARCHAR(100) NOT NULL,
    postal_code NVARCHAR(50) NULL,
    state_region NVARCHAR(100) NULL,
    country NVARCHAR(100) NOT NULL,
    is_primary BIT NOT NULL DEFAULT 0,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT FK_address_user FOREIGN KEY (user_id) REFERENCES dbo.user_account(id) ON DELETE CASCADE
);
GO

-- helpful index to quickly query the tables instead of reading through every single record.
CREATE INDEX IX_address_user on dbo.address(user_id);

--for any given user_id, there can be at most one row where the is_primary column is 1. And speeds up finding users' primary address
CREATE UNIQUE INDEX UX_address_user_primary ON dbo.address(user_id) WHERE is_primary=1;
GO