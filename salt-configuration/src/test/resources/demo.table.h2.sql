    CREATE TABLE DEMO
    (
      NAME            VARCHAR2(5) NOT NULL,
      VALUE          VARCHAR2(5) NOT NULL
    );

    ALTER TABLE DEMO
    ADD CONSTRAINT  DEMO_PK PRIMARY KEY (NAME);
