create function filtered_admin_gets(limitt integer, offsett integer, filterr text, asc_desc text)
    returns TABLE(id bigint, ime_smestaja text, drzava text, grad text, ocena double precision)
    language plpgsql
as
$$
declare
    query text;
begin
    query := 'select * from smestaj order by ' || filterr || ' ' || asc_desc || ' limit ' || limitt || ' offset 10*' || offsett;

    return query execute query;
end
$$;