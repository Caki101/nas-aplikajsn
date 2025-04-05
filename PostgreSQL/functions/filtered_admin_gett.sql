create function filtered_admin_gett(ids bigint[])
    returns TABLE(id bigint, smestaj_id bigint, sezona text, cena double precision, trajanje_odmora integer, broj_osoba integer, broj_tiketa integer, prevoz text, polazak timestamp without time zone, version integer)
    language plpgsql
as
$$
declare
    query text;
begin
    query := format(
            'select t.* from tiketi t inner join smestaj s on t.smestaj_id = s.id' ||
            case
                when array_length(ids, 1) is null then ''
                else ' WHERE t.id = ANY($1)'
                end
             );

    if array_length(ids, 1) is null then
        return query execute query;
    else
        return query execute query using ids;
    end if;
end
$$;