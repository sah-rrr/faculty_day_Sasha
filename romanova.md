***Легкие***
1. SELECT id, IF(has_internet = 1, "YES", "NO") as has_internet FROM Rooms
2. DELETE FROM Trip WHERE town_from = "Moscow"
3. SELECT DISTINCT p.name FROM Flights f 
   JOIN Pilots p ON f.second_pilot_id = p.pilot_id
   WHERE f.destination = 'New York'
   AND f.flight_date BETWEEN '2023-08-01' AND '2023-08-31'
4. SELECT DISTINCT status FROM FamilyMembers
   JOIN Payments ON member_id = family_member
   JOIN Goods ON good = good_id
   WHERE good_name = "potato"

***Средние***
1. SELECT good_name FROM Goods
   JOIN Payments ON good = good_id
   GROUP BY good_id, good_name
   HAVING COUNT(good_id)>1
2. SELECT FLOOR(AVG(TIMESTAMPDIFF(YEAR, birthday, NOW()))) AS age FROM FamilyMembers;
