create function bestoffers(limitt integer)
    returns TABLE(id bigint, smestaj_id bigint, sezona text, cena double precision, trajanje_odmora integer, broj_osoba integer, broj_tiketa integer, prevoz text, polazak timestamp without time zone, version integer)
    language plpgsql
as
$$
begin
    return query select t.*
                 from tiketi as t
                          join smestaj s
                               on s.id = t.smestaj_id
                 order by t.cena/s.ocena
                 limit limitt;
end
$$;