CREATE TABLE IF NOT EXISTS games (
                                     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                                     width INT NOT NULL,
                                     height INT NOT NULL,
                                     mines_count INT NOT NULL,
                                     completed BOOLEAN NOT NULL DEFAULT FALSE,
                                     game_field TEXT NOT NULL,
                                     user_field TEXT NOT NULL
);
