create function filtered_admin_gett(limitt integer, offsett integer, filterr text, asc_desc text)
    returns TABLE(id bigint, smestaj_id bigint, sezona text, cena double precision, trajanje_odmora integer, broj_osoba integer, broj_tiketa integer, prevoz text, polazak timestamp without time zone, version integer)
    language plpgsql
as
$$
declare
    query text;
begin
    filterr := 't.' || filterr;

    if filterr = 't.smestaj' then
        filterr := 's.ime_smestaja';
    end if;

    query := 'select t.* from tiketi t inner join smestaj s on t.smestaj_id = s.id order by ' ||
             filterr || ' ' || asc_desc || ' limit ' || limitt || ' offset 10*' || offsett;

    return query execute query;
end
$$;