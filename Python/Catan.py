from graphics import *
import math


colors = ["black", "red", "blue", "green", "orange", "darkblue", "yellow"]

# WIN_SIZE = 800
WIN_SIZE = 1100


def main():

    win_center = Point(WIN_SIZE//2, WIN_SIZE//2)
    # hex_size = 60
    hex_size = 120
    hex_size_short = hex_size * math.sqrt(3) / 2

    scale = Line(Point(WIN_SIZE//2-hex_size/2, 40), Point(WIN_SIZE//2+hex_size/2, 40))
    scale.setWidth(2)
    scale.setOutline("red")

    done = Circle(Point(20, 20), 15)
    done.setOutline("red")

    board = Board()
    board.place(win_center, hex_size)

    print()
    print(board)

    win = GraphWin("prototype", WIN_SIZE, WIN_SIZE, autoflush=False)
    done.draw(win)
    scale.draw(win)
    board.draw(win)
    win.update()
    win.autoflush = True

    click = win.getMouse()
    while not in_shape(done, click):
        board.get_vertex_click(click)
        # board.get_hex_click(click)
        click = win.getMouse()

    win.close()


class Board:
    def __init__(self, rings=2):
        self.rings = rings
        # hexes = [[None for i in range(rings*2+1)] for i in range(rings*2+1)]

        print('\n')  # DEBUG

        rings *= 2
        vertices = [[None for i in range(rings*2+1)] for j in range(rings*2+1)]
        num = 0
        for q in range(-rings, rings+1):
            for r in range(max(-rings,-q-rings), min(rings, -q+rings)+1):
                # print("placing V {:2} in {:2} {:2}".format(num, q, r), sep="")  # DEBUG
                if vertices[q][r] is not None:
                    print("DUPLICATE")  # DEBUG
                vertices[q][r] = Vertex(q, r)
                if abs(q-r) % 3 == 0:  # if |q-r| is a multiple of 3
                    hx_q, hx_r = q, r  # CONVERT THIS PROPERLY
                    print("hex {:2} at {:2}, {:2}  ->  {:2}, {:2}".format(num, q, r, hx_q, hx_r))
                    vertices[q][r] = Hex(q, r)

                    num += 1

        # self.hexes = hexes
        self.vertices = vertices

        print("\n")  # DEBUG

    def place(self, center, hex_size):
        self.hex_size = hex_size
        self.center = center
        self.hex_size_short = hex_size * math.sqrt(3) / 2
        # self.vertex_size = (hex_size-hex_size_short)*2 # 3

        for v_row in self.vertices:  # TODO use algorithm as in place()
            for v in v_row:
                if v is not None:
                    v.place(self.center, self.hex_size)

    def move(self, win, center):
        self.center = center
        self.undraw()
        self.place(center, self.hex_size)
        self.draw(win)

    def resize(self, win, hex_size):
        self.hex_size = hex_size
        self.undraw()
        self.place(self.center, hex_size)
        self.draw(win)

    def draw(self, win):  # TODO place() algo
        for v_row in self.vertices:
            for v in v_row:
                if v is not None:
                    v.draw(win)

    def undraw(self):  # TODO place() algo
        for v_row in self.vertices:
            for v in v_row:
                if v is not None:
                    v.undraw()

    def get_vertex_click(self, click):  # TODO might get a hex first. ensure vertex priority
        for v_row in self.vertices:
            for v in v_row:
                if v is not None and v.is_click_in(click):
                    print("(q, r) = ({:2},{:2})".format(v.q, v.r), sep="")  # DEBUG
                    self.vertices[v.q][v.r].setColor("red")
                    time.sleep(.35)
                    v.setColor("blue")
        # where = pixel_to_vertex(self.center, self.hex_size, click.getX(), click.getY())
        # where = PointToCoord(self.center, self.hex_size, click.x, click.y)
        # print('(',where.x, ', ', where.y, ')', sep="", end=" ")  # DEBUG
        # print('(',click.x, ', ', click.y, ')', sep="")  # DEBUG
        # if self.vertices[where.x][where.y] is not None:
        #     self.vertices[where.x][where.y].setColor("red")
        #     time.sleep(.35)
        #     self.vertices[where.x][where.y].setColor()#"light gray")

    def __str__(self):  # TODO use place() algo
        prt = ""
        for q in range(-len(self.vertices)//2+1, len(self.vertices)//2+1):
            for r in range(-len(self.vertices[q])//2+1, len(self.vertices[q])//2+1):
                if self.vertices[q][r] is not None:
                    prt += self.vertices[q][r].__str__() + " "
                else:
                    prt += "   ..   "
            prt += '\n'
        return prt


class Hex:
    def __init__(self, q, r, resource=None):
        self.resource = resource
        self.q, self.r = q, r
        self.text = str(q) + ", " + str(r)
        self.center = None

    def __str__(self):
        return "[{:2},{:2}]".format(self.q, self.r)

    def place(self, grid_center, size):
        self.size = size
        self.size_short = size * math.sqrt(3) / 2
        self.vertex_size = size//4
        self.vertex_size_short = self.vertex_size * math.sqrt(3) / 2

        # self.center = vertex_to_pixel(grid_center, self.size_short*2//3, self.q, self.r)
        self.center = vertex_to_pixel(grid_center, self.size, self.q, self.r)
        self.poly = Polygon(jump_linear(self.center, 0, self.size),  # size
                            jump_linear(self.center, 60, self.size),
                            jump_linear(self.center, 120, self.size),
                            jump_linear(self.center, 180, self.size),
                            jump_linear(self.center, 240, self.size),
                            jump_linear(self.center, 300, self.size))
        print(str(self) + ", distance = {:3}".format(distance(grid_center, self.center)))

        self.cir_center = Circle(self.center, 3)
        self.cir = Circle(self.center, self.size_short)
        self.cir_big = Circle(self.center, self.size)
        self.txt = Text(self.center, self.text)
        self.txt.setSize(int(max(min(self.vertex_size, 32), 5)))

    def draw(self, win):
        if self.center is not None:
            self.poly.draw(win)
            # self.cir.draw(win)
            self.txt.draw(win)
            # self.cir_big.draw(win)
    def undraw(self):
        if self.center is not None:
            self.poly.undraw()
            self.cir.undraw()
            self.txt.undraw()
            # self.cir_big.undraw()

    def is_click_in(self, point):
        x, y = point.getX(), point.getY()
        cx, cy = self.center.getX(), self.center.getY()
        # if radius < distance
        if self.size_short**2 > (cx-x)**2 + (cy-y)**2:
            return True
        return False

    def setColor(self, color="black"):
        if self.center is not None:
            self.cir.setOutline(color)
            self.poly.setOutline(color)
            self.txt.setTextColor(color)
    def setText(self, text):
        self.text = text
        self.txt.setText(text)


class Vertex:
    def __init__(self, q, r):
        self.q, self.r = q, r
        self.text = str(q) + ", " + str(r)
        # self.text = str(q+r)
        self.directions = [ (+1,  0), (+1, -1), ( 0, -1),
                            (-1,  0), (-1, +1), ( 0, +1)]

    def __str__(self):
        return "({:2},{:2})".format(self.q, self.r)

    def place(self, grid_center, size):
        self.size = size
        self.size_short = size * math.sqrt(3) / 2
        self.vertex_size = size//4
        self.vertex_size_short = self.vertex_size * math.sqrt(3) / 2

        # self.center = vertex_to_pixel(grid_center, self.size_short*2//3, self.q, self.r)
        self.center = vertex_to_pixel(grid_center, self.size, self.q, self.r)
        print(str(self) + ", distance = {:3}".format(distance(grid_center, self.center)))
        self.poly = Polygon(jump_linear(self.center, 30, self.vertex_size),  # vertex_size
                            jump_linear(self.center, 90, self.vertex_size),
                            jump_linear(self.center, 150, self.vertex_size),
                            jump_linear(self.center, 210, self.vertex_size),
                            jump_linear(self.center, 270, self.vertex_size),
                            jump_linear(self.center, 330, self.vertex_size))
        self.cir_center = Circle(self.center, 3)
        self.cir = Circle(self.center, self.vertex_size_short)
        self.cir_big = Circle(self.center, self.size)
        self.txt = Text(self.center, self.text)
        self.txt.setSize(int(max(min(self.vertex_size//2, 32), 5)))
        
    def draw(self, win):
        self.poly.draw(win)
        # self.cir.draw(win)
        # self.txt.draw(win)
        self.cir_center.draw(win)
        # self.cir_big.draw(win)
    def undraw(self, ):
        self.poly.undraw()
        # self.cir.undraw()
        self.txt.undraw()
        # self.cir_center.undraw()
        # self.cir_big.undraw()

    def is_click_in(self, point):
        x, y = point.getX(), point.getY()
        cx, cy = self.center.getX(), self.center.getY()
        # if radius < distance
        if self.vertex_size_short**2 > (cx-x)**2 + (cy-y)**2:
            return True
        return False
    def setColor(self, color="black"):
        self.cir.setOutline(color)
        self.poly.setOutline(color)
    def setText(self, text):
        self.text = text
        self.txt.setText(text)

    def neighbor(self, dir=0):
        return Point(self.q + self.directions[dir][0],
                     self.r + self.directions[dir][1])



def in_shape(cir, point):
    x, y = point.getX(), point.getY()
    cx, cy = cir.getCenter().getX(), cir.getCenter().getY()
    # if radius < distance
    if cir.getRadius()**2 > (cx-x)**2 + (cy-y)**2:
        return True
    return False

def jump_linear(point, angle, distance):
    # Calculates cartesian coordinates of destination,
    # given origin, angle to travel, and distance to travel
    x, y = point.getX(), point.getY()
    x += distance * math.cos(math.radians(angle))
    y -= distance * math.sin(math.radians(angle))
    return Point(x, y)

# def jump_hex(point, size, q, r):
#     # Calculates cartesian coordinates of destination,
#     # given cartesian origin coordinates and a hexagonal travel vector
#     x, y = point.getX(), point.getY()
#     qr_x = r * size * 3 / 2
#     qr_y = size * math.sqrt(3) * (q + r / 2)
#     return Point(x + qr_x, y + qr_y)
#
#
# def pixel_to_hex(center, size, x, y):
#     # calculates axial coordinates, given x,y coordinates and coordinates
#     # of the central hex
#     cx, cy = center.getX(), center.getY()
#     q = (x-cx) * 2/3 / size
#     r = (-(x-cx) / 3 + math.sqrt(3)/3 * (y-cy)) / size
#     return hex_round(q, r)
#
# def pixel_to_vertex(center, size, x, y):
#     # calculates axial coordinates, given x,y coordinates and coordinates
#     # of the central hex
#     cx, cy = center.getX(), center.getY()
#     q = ((x-cx) * math.sqrt(3)/3 - (y-cy) / 3) / size
#     r = (y-cy) * 2/3 / size
#     return hex_round(q, r)


# def vertex_to_pixel(center, size, q, r):
#     x = size * 3/2 * q
#     y = size * math.sqrt(3) * (r + q/2)
#     return Point(x + center.x, y + center.y)

def vertex_to_pixel(center, size, q ,r):
    x = size * (r + q/2)
    y = q * size * math.sqrt(3) / 2
    return Point(x + center.x, y + center.y)


def distance(p1, p2):
    return math.sqrt((p1.x-p2.x)**2 + (p1.y-p2.y)**2)


#
# #   q == x
# #   r == z
#
# #   y == y
#
# def hex_round(q, r):
#     # q, r are floating point axial coordinates
#     # rounds towards nearest integer hex coordinates
#     x, y, z = hex_to_cube(q, r)
#     q, y, r = cube_round(x, y, z)
#     return Point(q, r)
#
# def cube_round(q, y, r):
#     rx = round(q)
#     ry = round(y)
#     rz = round(r)
#
#     x_diff = abs(rx - q)
#     y_diff = abs(ry - y)
#     z_diff = abs(rz - r)
#
#     if x_diff > y_diff and x_diff > z_diff:
#         rx = -ry-rz
#     elif y_diff > z_diff:
#         ry = -rx-rz
#     else:
#         rz = -rx-ry
#
#     return (rx, ry, rz)
#
# def cube_to_hex(x, y, z): # axial
#     q = x
#     r = z
#     return Point(q, r)
#
# def hex_to_cube(q, r): # axial
#     y = -q-r
#     return (q, y, r)
#
#  # sorcery
# def PointToCoord(center, hex_size, x, z):
#
#     hex_size_short = hex_size * math.sqrt(3) / 2
#
#     # need to offset x, z by the center of the 0, 0 hex:
#     x -= center.x
#     z -= center.y
#
#     # x = (x - halfHexWidth) / hexWidth
#     x = (x - hex_size_short/2) / hex_size_short
#
#     # t1 = z / hexRadius
#     t1 = z / hex_size
#     t2 = math.floor(x + t1)
#     r = math.floor((math.floor(t1 - x) + t2) / 3)
#     q = math.floor((math.floor(2 * x + 1) + t2) / 3) - r
#     return Point(int(q), int(r))





main()