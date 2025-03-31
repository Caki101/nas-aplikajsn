create function filtered_admin_getu(limitt integer, offsett integer, filterr text, asc_desc text)
    returns TABLE(id bigint, email text, password text, username text, verified boolean)
    language plpgsql
as
$$
declare
    query text;
begin
    query := 'select * from users order by ' || filterr || ' ' || asc_desc || ' limit ' || limitt || ' offset 10*' || offsett;

    return query execute query;
end
$$;