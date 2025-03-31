create function tiketifilter(what text, which text, orderby text, page integer)
    returns TABLE(id bigint, smestaj_id bigint, sezona text, cena double precision, trajanje_odmora integer, broj_osoba integer, broj_tiketa integer, prevoz text, polazak timestamp without time zone, version integer)
    language plpgsql
as
$$
declare query TEXT;
    declare ids bigint[];
    declare offsett INTEGER default 0;

begin
    if orderBy not in ('sezona', 'drzava', 'grad', 'cena',
                       'trajanje_odmora', 'broj_osoba', 'broj_tiketa',
                       'prevoz', 'polazak') then
        raise exception 'Invalid column for ORDER BY: %', orderBy;
    end if;

    if page > 0 then
        offsett := page*10;
    end if;

    if what ILIKE 'drzava' or what ILIKE 'grad' or what ILIKE 'ocena' then
        query := 'SELECT array_agg(id) FROM smestaj WHERE ' || what || ' ILIKE ''' || which || '''';
        execute query into ids;

        what := 'smestaj_id';
    end if;

    if what ILIKE 'smestaj_id' then
        query := '(SELECT * FROM tiketi WHERE ' || what || ' = ANY($1)' ||
                 ' ORDER BY $2 OFFSET $3 LIMIT $4)' ||
                 ' UNION ALL ' ||
                 '(SELECT * FROM tiketi WHERE ' || what || ' = ANY($1)' ||
                 ' ORDER BY $2 DESC OFFSET $3 LIMIT $4)';

        return query execute query using ids, orderBy, offsett, 10;
    else
        query := '(SELECT * FROM tiketi WHERE $1 ILIKE $2' ||
                 ' ORDER BY $3 OFFSET $4 LIMIT $5)' ||
                 ' UNION ALL ' ||
                 '(SELECT * FROM tiketi WHERE $1 ILIKE $2' ||
                 ' ORDER BY $3 DESC OFFSET $4 LIMIT $5)';

        return query execute query using what, what, orderBy, offsett, 10;
    end if;
end
$$;