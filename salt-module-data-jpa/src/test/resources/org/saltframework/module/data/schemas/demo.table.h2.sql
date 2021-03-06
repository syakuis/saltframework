    CREATE TABLE DEMO
    (
      NAME            VARCHAR2(5) NOT NULL,
      VALUE          VARCHAR2(5) NOT NULL
    );

    ALTER TABLE DEMO
    ADD CONSTRAINT  DEMO_PK PRIMARY KEY (NAME);

    CREATE TABLE MEMBER
    (
      USER_ID            VARCHAR2(5) NOT NULL,
      USER_NAME          VARCHAR2(5) NOT NULL,
      REG_DATE           DATETIME NULL,
    );

    ALTER TABLE MEMBER
    ADD CONSTRAINT  MEMBER_PK PRIMARY KEY (USER_ID);

    CREATE TABLE USER
    (
      ID           NUMBER NOT NULL IDENTITY,
      USER_ID            VARCHAR2(5) NOT NULL,
      PHONE_NUMBER       VARCHAR2(5)  NULL,
      ADDRESS            VARCHAR2(5)  NULL
    );

--     ALTER TABLE USER
--     ADD CONSTRAINT  USER_PK PRIMARY KEY (ID);
