
BEGIN TRANSACTION;

DROP TABLE IF EXISTS users                      cascade;
DROP TABLE IF EXISTS user_profile               cascade;
DROP TABLE IF EXISTS training                   cascade;
DROP TABLE IF EXISTS cert_period                cascade;
DROP TABLE IF EXISTS training_cert_period       cascade;

CREATE TABLE users(
    id serial ,
    email varchar(64) NOT NULL UNIQUE,
    password varchar(32) NOT NULL,
    salt varchar(256) NOT NULL,
    permission varchar(32),
    CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE TABLE user_profile(
    profile_id integer NOT NULL,
    firstname varchar(32) NOT NULL,
    lastname varchar(32) NOT NULL,
    role varchar(255) NOT NULL,
    start_date date NOT NULL,
    end_date date,
    profile_pic varchar(255),
    CONSTRAINT uq_user_profile_profile_id UNIQUE (profile_id)
);

CREATE TABLE training(
    train_id serial ,
    train_name varchar(255) NOT NULL,
    train_provider varchar(255) NOT NULL,
    train_topic varchar(800) NOT NULL,
    train_date date NOT NULL,
    is_compliance boolean NOT NULL,
    train_proof varchar(255),
    minutes integer NOT NULL,
    CONSTRAINT pk_training_train_id PRIMARY KEY (train_id)
);

CREATE TABLE cert_period(
    cert_id serial,
    profile_id integer NOT NULL,
    cert_start_date date NOT NULL,
    CONSTRAINT pk_cert_period_cert_id PRIMARY KEY (cert_id)
);

CREATE TABLE training_cert_period (
    train_id integer NOT NULL,
    cert_period_id integer NOT NULL,
    CONSTRAINT pk_training_cert_period_train_id_cert_period_id PRIMARY KEY (train_id, cert_period_id)
);

-- Password is 'greatwall'
BEGIN TRANSACTION;
INSERT INTO users (email, password, salt, permission) 
        VALUES ('matt.goshorn@techelevator.com','FjZDm+sndmsdEDwNtfr6NA==',
                'kidcasB0te7i0jK0fmRIGHSm0mYhdLTaiGkEAiEvLp7dAEHWnuT8n/5bd2V/mqjstQ198iImm1xCmEFu+BHyOz1Mf7vm4LILcrr17y7Ws40Xyx4FOCt8jD03G+jEafpuVJnPiDmaZQXJEpEfekGOvhKGOCtBnT5uatjKEuVWuDA=','admin');

INSERT INTO training (train_name, train_provider, train_topic, train_date, is_compliance, train_proof, minutes) 
        VALUES('LEARNING HOW TO TEACH', 'YMCA', 'We teach people how to teach, its very educational.', '2020-02-13',true, null, 90);

INSERT INTO cert_period (profile_id, cert_start_date) 
        VALUES ((SELECT id FROM users WHERE users.email ='matt.goshorn@techelevator.com'), '2019-10-01');

INSERT INTO user_profile (profile_id, firstname, lastname, role, start_date, end_date, profile_pic) 
        VALUES ((SELECT id FROM users WHERE email = 'matt.goshorn@techelevator.com' ),'Matt','Goshorn', 'Instructor', '2020-01-13', null, null);

INSERT INTO training_cert_period (train_id, cert_period_id)
        VALUES ((SELECT train_id FROM training WHERE training.train_name='LEARNING HOW TO TEACH'), 1);
        
        
   SELECT * FROM training 
        JOIN training_cert_period ON training.train_id = training_cert_period.train_id
        JOIN cert_period ON training_cert_period.cert_period_id = cert_period.cert_id
    WHERE cert_period.profile_id = 1;

ALTER TABLE user_profile
ADD FOREIGN KEY(profile_id)
REFERENCES users(id);

ALTER TABLE cert_period
ADD FOREIGN KEY(profile_id)
REFERENCES user_profile(profile_id);

ALTER TABLE training_cert_period 
ADD FOREIGN KEY(train_id)
REFERENCES training(train_id);

ALTER TABLE training_cert_period 
ADD FOREIGN KEY(cert_period_id)
REFERENCES cert_period(cert_id);

COMMIT;

Delete from user_profile
where profile_id = 2


delete from user_profile
where profile_id = 3;
delete from users
where id = 3;

DELETE  FROM training_cert_period;
DELETE  FROM training;


update user_profile
--set profile_pic = 'https://res.cloudinary.com/goshorn/image/upload/v1593971293/lms_test/yskdldqgqnvu7jieywyr.jpg' --my image
--set profile_pic = 'https://res.cloudinary.com/goshorn/image/upload/v1594063575/lms_test/lbwlhmexahogdgupwczw.jpg'-- bobs image
--                  'https://res.cloudinary.com/goshorn/image/upload/v1594131446/lms_test/yxk4g9xnsmmxpeo7mxjo.jpg'-- bill's image





