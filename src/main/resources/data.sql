INSERT INTO public.genres VALUES (1, 'Drama');
INSERT INTO public.genres VALUES (2, 'Action');
INSERT INTO public.genres VALUES (3, 'Mystery');
INSERT INTO public.genres VALUES (4, 'Sci-Fi');
INSERT INTO public.genres VALUES (5, 'Comedy');
INSERT INTO public.genres VALUES (6, 'Crime');
INSERT INTO public.genres VALUES (7, 'Adventure');

INSERT INTO public.ratings VALUES (1, 'G', 'All ages admitted. Nothing that would offend parents for viewing by children.');
INSERT INTO public.ratings VALUES (2, 'PG', 'Some material may not be suitable for children. Parents urged to give "parental guidance". May contain some material parents might not like for their young children.');
INSERT INTO public.ratings VALUES (3, 'PG-13', 'Some material may be inappropriate for children under 13. Parents are urged to be cautious. Some material may be inappropriate for pre-teenagers.');
INSERT INTO public.ratings VALUES (4, 'R', 'Under 17 requires accompanying parent or adult guardian. Contains some adult material. Parents are urged to learn more about the film before taking their young children with them.');
INSERT INTO public.ratings VALUES (5, 'NC-17', 'No one 17 and under admitted. Clearly adult. Children are not admitted.');

INSERT INTO public.films VALUES (1, 'Prometheus', 'Following clues to the origin of mankind, a team finds a structure on a distant moon, but they soon realize they are not alone.', '2012-05-31', 124, 4);
INSERT INTO public.films VALUES (2, 'Fight Club', 'An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.', '2000-01-13', 139, 4);

INSERT INTO public.film_genres VALUES (2, 1);
INSERT INTO public.film_genres VALUES (1, 7);
INSERT INTO public.film_genres VALUES (1, 3);
INSERT INTO public.film_genres VALUES (1, 4);

INSERT INTO public.statuses VALUES (1, 'Not confirmed');
INSERT INTO public.statuses VALUES (2, 'Confirmed');

INSERT INTO public.users VALUES (1, 'kostrykinmark@gmail.com', 'Leather_Bastard', 'Mark Kostrykin', '1997-09-02');

INSERT INTO public.likes VALUES (1, 1);
INSERT INTO public.likes VALUES (2, 1);