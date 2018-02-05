import random
import math
import os

seed = 819374816358
number_of_points = 50  # between 2 and 100
coordinate_limit = 1000
points = list()
random.seed(seed)

pass_file = open('pass.data', 'w+')
fail_file = open('fail.data', 'w+')

# Number of points
pass_file.write(str(number_of_points) + "\n")
fail_file.write(str(number_of_points) + "\n")


# Coordinates
for p in range(number_of_points):
    point = (random.randint(0, coordinate_limit), random.randint(0, coordinate_limit))
    points.append(point)
    pass_file.write(str(point[0]) + " ")
    pass_file.write(str(point[1]) + "\n")
    fail_file.write(str(point[0]) + " ")
    fail_file.write(str(point[1]) + "\n")

# PARAMETERS
# length1

# mean calculation that is not used
# mean_sum = 0
# for i in range(0, number_of_points - 1):
#     x1 = points[i][0]
#     x2 = points[i + 1][0]
#     y1 = points[i][1]
#     y2 = points[i + 1][1]
#     dist = math.sqrt((x2 - x1)**2 + (y2 - y1)**2)
#     mean_sum += dist
# mean = mean_sum / number_of_points - 1
# pass_file.write(str(mean) + "\n")

pass_file.write("1" + "\n")
fail_file.write(str(coordinate_limit * 2) + "\n")

# radius1
pass_file.write(str(coordinate_limit) + "\n")
fail_file.write("1" + "\n")

# epsilon
pass_file.write("0.001" + "\n")
fail_file.write("0.001" + "\n")

# area1
pass_file.write("1" + "\n")
fail_file.write("1000" + "\n")

# q_pts
pass_file.write("2" + "\n")
fail_file.write("10" + "\n")

# quads
pass_file.write("1" + "\n")
fail_file.write("3" + "\n")

# dist
pass_file.write("1" + "\n")
fail_file.write(str(coordinate_limit) + "\n")

# n_pts
pass_file.write("3" + "\n")
fail_file.write("10" + "\n")

# k_pts
pass_file.write("1" + "\n")
fail_file.write("10" + "\n")

# NOTE! ALL THE PARAMETER VALUES BELOW HAVE NOT GOTTEN THAT MUCH CONSIDERATION
# a_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# b_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# c_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# d_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# e_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# f_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# g_pts
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# length2
pass_file.write("1000" + "\n")
fail_file.write("1" + "\n")

# radius2
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# area2
pass_file.write("1" + "\n")
fail_file.write("1" + "\n")

# LCM (Logical Connector Matrix)
random.seed(1476817648173764)  # new seed to decide
for i in range(15):
    row = ""
    for j in range(15):
        logic_operator = "NONE"
        random_number = random.randint(0, 2)
        if(random_number is 0):
            logic_operator = "ANDD"
        elif(random_number is 1):
            logic_operator = "ORR"
        else:
            logic_operator = "NOTUSED"
        row += logic_operator + " "
    pass_file.write(row + "\n")
    fail_file.write(row + "\n")

# PUV (Preliminary Unlocking Vector)
random.seed(892348236572479)  # new seed to decide
row = ""
for v in range(15):
    vector_value = "NONE"
    random_number = random.randint(0, 1)
    if(random_number is 0):
        vector_value = "true"
    else:
        vector_value = "false"
    row += vector_value + " "
pass_file.write(row + "\n")
fail_file.write(row + "\n")


