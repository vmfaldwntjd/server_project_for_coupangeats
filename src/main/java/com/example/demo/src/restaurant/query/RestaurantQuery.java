package com.example.demo.src.restaurant.query;


public class RestaurantQuery {

    public static String getFNRestaurantListQuery = "SELECT R.restaurant_id,\n" +
            "       res_name,\n" +
            "       res_image_url,\n" +
            "       delivery_time,\n" +
            "       IFNULL(star_point, 0) as star_point,\n" +
            "       IFNULL(review_count, 0) as review_count,\n" +
            "       round(distance/1000, 1) as distance,\n" +
            "       IFNULL(min_delivery_fee, 0) as min_delivery_fee # 배달비 설정이 되어있지 않다면 무료배달로 간주\n" +
            "FROM (\n" +
            "    SELECT restaurant_id,\n" +
            "            restaurant_name as res_name,\n" +
            "            ST_Distance_Sphere(POINT(?,?), POINT(longitude, latitude)) as distance,\n" +
            "            delivery_time\n" +
            "    FROM restaurant\n" +
            ") R\n" +
            "left join (\n" +
            "    SELECT restaurant_id,\n" +
            "           AVG(star_point) as star_point,\n" +
            "           COUNT(restaurant_id) as review_count\n" +
            "    FROM review\n" +
            "    GROUP BY restaurant_id\n" +
            ") RV ON R.restaurant_id = RV.restaurant_id\n" +
            "left join (\n" +
            "    SELECT restaurant_id,\n" +
            "        MIN(delivery_fee) as min_delivery_fee\n" +
            "    FROM res_delivery_fee\n" +
            "    GROUP BY restaurant_id\n" +
            ") RDF ON R.restaurant_id = RDF.restaurant_id\n" +
            "join (\n" +
            "    SELECT restaurant_id,\n" +
            "           url as res_image_url\n" +
            "    FROM res_image\n" +
            "    WHERE image_id = 1\n" +
            ") I ON R.restaurant_id = I.restaurant_id\n" +
            "join (\n" +
            "    SELECT restaurant_id,\n" +
            "           category_id\n" +
            "    FROM res_category\n" +
            "    WHERE category_id = ?\n" +
            ") RC ON R.restaurant_id = RC.restaurant_id\n" +
            "WHERE distance < 3000\n" +
            "ORDER BY star_point DESC \n" +
            "LIMIT 10;";



    public static String getRestaurantListByCategoryIdQuery(String sortBy, String orderBy){
        return getRestaurantSortByQuery + "\n" + joinCategoryIdQuery +"\n"+"WHERE distance < 3000\n" +
                "ORDER BY "+sortBy+" "+orderBy+"\n" +
                "LIMIT ?;";
    }

    public static String getRestaurantListQuery(String sortBy, String orderBy){
        return getRestaurantSortByQuery +"\n"+"WHERE distance < 3000\n" +
                "ORDER BY "+sortBy+" "+orderBy+"\n" +
                "LIMIT ?;";
    }


    // 현재 위치를 기준으로 세네번째 파라미터에 들어가는 string 값에 따라 최대 상위 45개의 값 출력.
    // 가까운순(distance, ASC), 별점 높은 순(star_point, DESC), 신규 매장순(created_at, DESC).
    public static String getRestaurantSortByQuery ="SELECT R.restaurant_id,\n" +
            "       created_at,\n" +
            "       res_name,\n" +
            "       is_cheetah,\n" +
            "       delivery_time,\n" +
            "       IFNULL(star_point, 0)       as star_point,\n" +
            "       IFNULL(review_count, 0)     as review_count,\n" +
            "       round(distance / 1000, 1)   as distance,\n" +
            "       IFNULL(min_delivery_fee, 0) as min_delivery_fee # 배달비 설정이 되어있지 않다면 무료배달로 간주\n" +
            "FROM (\n" +
            "         SELECT restaurant_id,\n" +
            "                created_at,\n" +
            "                restaurant_name                                             as res_name,\n" +
            "                is_cheetah,\n" +
            "                ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) as distance,\n" +
            "                delivery_time\n" +
            "         FROM restaurant\n" +
            "     ) R\n" +
            "         left join (\n" +
            "    SELECT restaurant_id,\n" +
            "           AVG(star_point)      as star_point,\n" +
            "           COUNT(restaurant_id) as review_count\n" +
            "    FROM review\n" +
            "    GROUP BY restaurant_id\n" +
            ") RV ON R.restaurant_id = RV.restaurant_id\n" +
            "         left join ( \n" +
            "    SELECT restaurant_id,\n" +
            "           MIN(delivery_fee) as min_delivery_fee\n" +
            "    FROM res_delivery_fee\n" +
            "    GROUP BY restaurant_id\n" +
            ") RDF ON R.restaurant_id = RDF.restaurant_id";

    // 카테고리별 검색시 덧붙여 사용.
    public static String joinCategoryIdQuery = "join (\n" +
            "    SELECT restaurant_id,\n" +
            "           category_id\n" +
            "    FROM res_category\n" +
            "    WHERE category_id = ?\n" +
            ") RC ON R.restaurant_id = RC.restaurant_id";

//    // 왜인지 적용이 안됨.
//    // 카테고리별 화면 = LIMIT 100, 메인 화면 LIMIT = 45
//    public static String commonQuery = "WHERE distance < 3000\n" +
//            "ORDER BY ? ?\n" +
//            "LIMIT ?;";


    // 가게별 메인 화면. 가게별 id를 통한 단 하나의 가게 조회.
    // 계산된 거리에 따라서 delivery_time 재계산이 필요함...
    public static String getRestaurantByIdQuery = "SELECT R.restaurant_id,\n" +
            "       created_at,\n" +
            "       res_name,\n" +
            "       is_cheetah,\n" +
            "       delivery_time,\n" +
            "       IFNULL(star_point, 0)      as star_point,\n" +
            "       IFNULL(review_count, 0)    as review_count,\n" +
            "       IFNULL(min_delivery_fee, 0) as min_delivery_fee, # 배달비 설정이 되어있지 않다면 무료배달로 간주 \n" +
            "       IFNULL(min_order_price, 0) as min_order_price\n" +
            "FROM (\n" +
            "         SELECT restaurant_id,\n" +
            "                created_at,\n" +
            "                restaurant_name                                             as res_name,\n" +
            "                is_cheetah,\n" +
            "                ST_Distance_Sphere(POINT(?, ?), POINT(longitude, latitude)) as distance,\n" +
            "                delivery_time\n" +
            "         FROM restaurant\n" +
            "         WHERE restaurant_id = ?\n" +
            "     ) R\n" +
            "         left join (\n" +
            "    SELECT restaurant_id,\n" +
            "           AVG(star_point)      as star_point,\n" +
            "           COUNT(restaurant_id) as review_count\n" +
            "    FROM review\n" +
            "    GROUP BY restaurant_id\n" +
            ") RV ON R.restaurant_id = RV.restaurant_id\n" +
            "         left join (\n" +
            "    SELECT restaurant_id,\n" +
            "           MIN(delivery_fee) as min_delivery_fee,\n" +
            "           MIN(min_price)    as min_order_price\n" +
            "    FROM res_delivery_fee\n" +
            "    GROUP BY restaurant_id\n" +
            ") RDF ON R.restaurant_id = RDF.restaurant_id;";

    public static String getResImageUrlByIdQuery = "SELECT url as res_image_Url\n" +
            "FROM res_image\n" +
            "WHERE restaurant_id = ? AND hide_flag = 0\n" +
            "ORDER BY image_id ASC;";

    public static String getResKindQuery = "SELECT kind_id,\n" +
            "       kind_name \n" +
            "FROM res_kind\n" +
            "WHERE restaurant_id = ?\n" +
            "  AND status = 1\n" +
            "  AND is_option = 0\n" +
            "ORDER BY kind_id ASC;";

    public static String getResKindMenuQuery = "SELECT RK.kind_id,\n" +
            "       kind_name,\n" +
            "       RM.menu_id,\n" +
            "       menu_name,\n" +
            "       menu_price,\n" +
            "       menu_description,\n" +
            "       url as menu_image_url\n" +
            "FROM ( # 가게에 등록된 대분류 정보를 불러온다.\n" +
            "         SELECT restaurant_id,\n" +
            "                kind_id,\n" +
            "                kind_name as kind_name\n" +
            "         FROM res_kind\n" +
            "         WHERE restaurant_id = ?\n" +
            "           AND status = 1\n" +
            "           AND is_option = 0\n" +
            "         ORDER BY kind_id ASC\n" +
            "     ) RK\n" +
            "         join ( # 해당 가게 대분류에 포함되는 menu 정보를 찾는다.\n" +
            "    SELECT restaurant_id,\n" +
            "           kind_id,\n" +
            "           menu_id\n" +
            "    FROM res_kind_menu\n" +
            ") RKM ON RK.restaurant_id = RKM.restaurant_id AND RK.kind_id = RKM.kind_id\n" +
            "         join ( # 각 메뉴에 해당하는 상세 정보를 찾는다.\n" +
            "    SELECT restaurant_id,\n" +
            "           menu_id,\n" +
            "           name        as menu_name,\n" +
            "           description as menu_description,\n" +
            "           price       as menu_price\n" +
            "    FROM res_menu\n" +
            ") RM ON RK.restaurant_id = RM.restaurant_id AND RKM.menu_id = RM.menu_id\n" +
            "         left join ( # 각 메뉴에 해당하는 대표 이미지를 찾는다.\n" +
            "    SELECT restaurant_id,\n" +
            "           menu_id,\n" +
            "           url\n" +
            "    FROM res_menu_image\n" +
            "    WHERE image_id = 1\n" +
            ") RMI ON RK.restaurant_id = RMI.restaurant_id AND RKM.menu_id = RMI.menu_id\n" +
            "ORDER BY kind_id, menu_id ASC;";


    public static String getResMenuQuery = "SELECT\n" +
            "menu_id,\n" +
            "       name as menu_name,\n" +
            "       price as menu_price,\n" +
            "       description as menu_description\n" +
            "From res_menu\n" +
            "WHERE restaurant_id = ? AND menu_id = ?;\n";

    public static String getResMenuImageUrlListQuery ="SELECT url as menu_image_url,\n" +
            "       image_id       \n" +
            "FROM res_menu_image\n" +
            "WHERE restaurant_id = ? AND menu_id = ?\n" +
            "ORDER BY image_id";

    public static String getResMenuKindQuery = "SELECT RK.kind_id as option_kind_id,\n" +
            "       kind_name  as option_kind_name,\n" +
            "       max_count  as option_kind_max_count,\n" +
            "       is_essential\n" +
            "FROM ( # 현재 가게의 현재 선택한 특정 메뉴를 참조하는 모든 옵션 kind를 찾는다.\n" +
            "         SELECT restaurant_id,\n" +
            "                parent_menu_id as menu_id,\n" +
            "                kind_id\n" +
            "         FROM res_menu_option_kind\n" +
            "         WHERE restaurant_id = ?\n" +
            "           AND parent_menu_id = ?\n" +
            "     ) RMOK\n" +
            "         join ( # 각 kind에 대해 이름과 필수 여부를 찾는다.\n" +
            "    SELECT restaurant_id,\n" +
            "           kind_id,\n" +
            "           kind_name,\n" +
            "           is_essential,\n" +
            "           max_count\n" +
            "    FROM res_kind\n" +
            ") RK ON RK.kind_id = RMOK.kind_id AND RMOK.restaurant_id = RK.restaurant_id;\n";


    public static String getResMenuOptionQuery = "SELECT option_id,\n" +
            "       option_name,\n" +
            "       option_price\n" +
            "FROM (SELECT restaurant_id,\n" +
            "             menu_id\n" +
            "      FROM res_kind_menu\n" +
            "      WHERE restaurant_id = ?\n" +
            "        AND kind_id = ?\n" +
            "     ) RKM\n" +
            "         join (\n" +
            "    SELECT restaurant_id,\n" +
            "           menu_id as option_id,\n" +
            "           name  as option_name,\n" +
            "           price as option_price\n" +
            "    FROM res_menu\n" +
            ") RM ON RKM.restaurant_id = RM.restaurant_id AND RKM.menu_id = RM.option_id;";

    public static String getResDistance = "SELECT ST_Distance_Sphere(POINT(?,?), POINT(longitude, latitude)) as distance\n" +
            "FROM restaurant R join (\n" +
            "             SELECT restaurant_id\n" +
            "             FROM cart\n" +
            "             WHERE cart_id = ?\n" +
            ") C ON R.restaurant_id = C.restaurant_id";

    // cart에 존재하는 res에 대해, 주문 금액에 따라 배달비 산출, 거리에 따라 배달 시간 조절.
    public static String getRestaurantInfoQuery = "SELECT C.restaurant_id,\n" +
            "                   restaurant_name,\n" +
            "                   is_cheetah,\n" +
            "                   is_packable,\n" +
            "                   delivery_time,\n" +
            "                   packaging_time,\n" +
            "                   IFNULL(delivery_fee, 0) as delivery_fee, # 배달비 설정이 되어있지 않다면 무료배달로 간주\n" +
            "                   min_order_price\n" +
            "            FROM (\n" +
            "                SELECT restaurant_id\n" +
            "                FROM cart\n" +
            "                WHERE cart_id = ? #cart_id\n" +
            "                ) C join\n" +
            "            (\n" +
            "                SELECT restaurant_id,\n" +
            "                   restaurant_name,\n" +
            "                   is_cheetah,\n" +
            "                   is_packable,\n" +
            "                   IF( ? > 3000, delivery_time+5, delivery_time) as delivery_time, # distance\n" +
            "                   packaging_time\n" +
            "            FROM restaurant\n" +
            "            )  R ON C.restaurant_id = R.restaurant_id\n" +
            "            left join (\n" +
            "            SELECT restaurant_id,\n" +
            "                   delivery_fee\n" +
            "            FROM res_delivery_fee\n" +
            "            WHERE ? BETWEEN min_price AND max_price #orderPrice\n" +
            "            )  RDF ON C.restaurant_id = RDF.restaurant_id\n" +
            "            join (\n" +
            "                SELECT MIN(min_price) as min_order_price\n" +
            "                FROM res_delivery_fee\n" +
            "            ) RDF2;";
}
