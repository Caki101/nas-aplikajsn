create function insert_admin(idd bigint) returns boolean
    language plpgsql
as
$$
declare result users%rowtype;
begin
    insert into admins (user_id)
    select idd
    where not exists (
        select 1 from admins a where a.user_id = idd
    )
    RETURNING * INTO result;

    IF FOUND THEN
        RETURN true;
    END IF;

    return false;
end;
$$;