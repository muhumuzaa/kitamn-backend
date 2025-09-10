ALTER TABLE dbo.address
DROP COLUMN is_primary;

DROP INDEX UX_address_user_primary ON dbp.address;

CREATE UNIQUE INDEX UX_address_user ON dbo.address(user_id);