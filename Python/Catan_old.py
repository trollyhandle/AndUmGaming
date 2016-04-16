from graphics import *
import math

#import Shapes

colors = ["black", "red", "blue", "green", "orange", "darkblue", "yellow"]

WIN_SIZE = 800


def main():
    win = GraphWin("hi", WIN_SIZE, WIN_SIZE)
    win_center = Point(WIN_SIZE//2, WIN_SIZE//2)
    hex_size = 85
    hex_size_short = hex_size * math.sqrt(3) / 2

    # cir = Circle(win_center, hex_size) # circumscribed circle on center hex
    # cir.setFill("grey")
    # cir.draw(win)
    done = Circle(Point(20, 20), 15)
    done.setOutline("red")
    done.draw(win)

    board = Board()
    board.create(win_center, hex_size)
    board.draw(win)
    click = win.getMouse()
    while not in_shape(done, click):
        board.get_click(click)
        click = win.getMouse()

    win.getMouse()
    win.close()


class Board:
    def __init__(self):
        rings = 3


    def create(self, center, hex_size):
        self.hex_size = hex_size
        self.center = center
        hex_size_short = hex_size * math.sqrt(3) / 2
        vertex_size = (hex_size-hex_size_short)*2 # 3

        hexes = [[None for i in range(5)] for i in range(5)]
        # num = 0
        # for z in range(-2, 3):
        #     for q in range(-2, 3):
        #         for r in range(-2,3):
        #             if z+q+r == 0:
        #                 hx_center = jump_linear(center, z*60+30, hex_size_short*2)
        #                 hx_coord = rect_to_hex(center, hex_size, hx_center.x, hx_center.y)
        #                 print("placing H ", num, " in ", hx_coord.x, " ", hx_coord.y, sep="")
        #                 hexes[hx_coord.x][hx_coord.y] = Hex(hx_center, hex_size, hx_coord)
        #                 num += 1

        hexes[0][0] = Hex(center, hex_size, Point(0,0))
        for i in range(6):
            hx_center = jump_linear(center, i*60+30, hex_size_short*2)
            hx_coord = rect_to_hex(center, hex_size, hx_center.x, hx_center.y)
            hexes[hx_coord.x][hx_coord.y] = Hex(hx_center, hex_size, hx_coord)
        for i in range(12):
            hx_center = jump_linear(center, i*30, hex_size_short*4 if i%2 else hex_size*3)
            hx_coord = rect_to_hex(center, hex_size, hx_center.x, hx_center.y)
            hexes[hx_coord.x][hx_coord.y] = Hex(hx_center, hex_size, hx_coord)
        self.hexes = hexes



        # vertices = [[None for i in range(5)] for i in range(5)]
        # # vertices[0][0] = Vertex(center, vertex_size)
        # vx_center = center
        # vx_coord = rect_to_vertex(center, hex_size, vx_center.x, vx_center.y)
        # print("placing V in", vx_coord.x, vx_coord.y)
        # vertices[vx_coord.x][vx_coord.y] = Vertex(vx_center, vertex_size, vx_coord)
        #
        # for i in range(6):
        #     vx_center = jump_linear(center, i*60, hex_size)
        #     vx_coord = rect_to_vertex(center, hex_size, vx_center.x, vx_center.y)
        #     print("placing V in", vx_coord.x, vx_coord.y)
        #     vertices[vx_coord.x][vx_coord.y] = Vertex(vx_center, vertex_size, vx_coord)
        #     # vertices[vx_coord.x][vx_coord.y].setText(str(i))
        # for i in range(12):
        #     vx_center = jump_linear(center, i*30, hex_size*5/2) # TODO
        #     vx_coord = rect_to_vertex(center, hex_size, vx_center.x, vx_center.y)
        #     print("placing V in", vx_coord.x, vx_coord.y)
        #     vertices[vx_coord.x][vx_coord.y] = Vertex(vx_center, vertex_size, vx_coord)
        #     vertices[vx_coord.x][vx_coord.y].setText(str(i))
        #     vx_center = jump_linear(center, i*30, hex_size_short*4 if i%2 else hex_size*3)
        #     vx_coord = rect_to_hex(center, hex_size, vx_center.x, vx_center.y)
        #     vertices[vx_coord.x][vx_coord.y] = Vertex(vx_center, vertex_size, vx_coord)
        # self.vertices = vertices

    def draw(self, win):
        for hex_row in self.hexes:
            for hex in hex_row:
                if hex is not None:
                    # hex.setColor("light gray")
                    hex.draw(win)
        # for v_row in self.vertices:
        #     for v in v_row:
        #         if v is not None:
        #             v.draw(win)

    def get_click(self, click):
        where = rect_to_hex(self.center, self.hex_size, click.getX(), click.getY())
        print('(',where.x, ', ', where.y, ')', sep="")
        self.hexes[where.x][where.y].setColor("red")
        time.sleep(.35)
        self.hexes[where.x][where.y].setColor()
    # represent hexes in either cubic coordinates or
    #







class Hex:
    def __init__(self, center, size, hx_coord, resource=None):
        self.vertices = [None, None, None, None, None, None]
        self.resource = resource
        self.hx_coord = hx_coord
        self.text = str(hx_coord.x) + ", " + str(hx_coord.y)
        self.size = size
        self.size_short = size * math.sqrt(3) / 2
        self.center = center
        self.poly = Polygon(jump_linear(center, 0, size),
                            jump_linear(center, 60, size),
                            jump_linear(center, 120, size),
                            jump_linear(center, 180, size),
                            jump_linear(center, 240, size),
                            jump_linear(center, 300, size))
        for i in range(6):
            self.vertices[i] = Vertex(jump_linear(center, i*(60), size), (size-self.size_short)*2)

        self.cir = Circle(center, self.size_short)
        self.cir_big = Circle(center, self.size)
        self.txt = Text(center, self.text)
        self.txt.setSize(max(size//5, 5))

    def draw(self, win, color="black"):
        self.poly.draw(win)
        self.cir.draw(win)
        self.txt.draw(win)
        # self.cir_big.draw(win)

    def is_click_in(self, point):
        x, y = point.getX(), point.getY()
        cx, cy = self.center.getX(), self.center.getY()
        # if radius < distance
        if self.size_short**2 > (cx-x)**2 + (cy-y)**2:
            return True
        return False

    def setColor(self, color="black"):
        self.cir.setOutline(color)
        self.poly.setOutline(color)
        self.txt.setTextColor(color)

class Vertex:
    def __init__(self, center, size, vx_coord=Point(0,0)):
        # size //= 3
        self.hx_coord = vx_coord
        self.text = str(vx_coord.x) + ", " + str(vx_coord.y)
        self.size = size
        self.size_short = size * math.sqrt(3) / 2
        self.center = center
        self.poly = Polygon(jump_linear(center, 30, size),
                            jump_linear(center, 90, size),
                            jump_linear(center, 150, size),
                            jump_linear(center, 210, size),
                            jump_linear(center, 270, size),
                            jump_linear(center, 330, size))

        self.cir = Circle(center, self.size_short)
        self.cir_big = Circle(center, self.size)
        self.txt = Text(center, self.text)
        self.txt.setSize(int(max(min(size//2, 32), 5)))


    def draw(self, win):
        self.poly.draw(win)
        self.cir.draw(win)
        self.txt.draw(win)
        # self.cir_big.draw(win)

    def is_click_in(self, point):
        x, y = point.getX(), point.getY()
        cx, cy = self.center.getX(), self.center.getY()
        # if radius < distance
        if self.size_short**2 > (cx-x)**2 + (cy-y)**2:
            return True
        return False

    def setColor(self, color="black"):
        self.cir.setOutline(color)
        self.poly.setOutline(color)
    def setText(self, text):
        self.text = text
        self.txt.setText(text)



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

def jump_hex(point, size, q, r):
    # Calculates cartesian coordinates of destination,
    # given cartesian origin coordinates and a hexagonal travel vector
    x, y = point.getX(), point.getY()
    qr_x = r * size * 3 / 2
    qr_y = size * math.sqrt(3) * (q + r / 2)
    return Point(x + qr_x, y + qr_y)


def rect_to_hex(center, size, x, y):
    # calculates axial coordinates, given x,y coordinates and coordinates
    # of the central hex
    cx, cy = center.getX(), center.getY()
    q = (x-cx) * 2/3 / size
    r = (-(x-cx) / 3 + math.sqrt(3)/3 * (y-cy)) / size
    q = math.floor(q+.5) if q > 0 else math.ceil(q-.5)
    r = math.floor(r+.5) if r > 0 else math.ceil(r-.5)
    return Point(q, r)

def rect_to_vertex(center, size, x, y):
    # calculates axial coordinates, given x,y coordinates and coordinates
    # of the central hex
    cx, cy = center.getX(), center.getY()
    q = ((x-cx) * math.sqrt(3)/3 - (y-cy) / 3) / size
    r = (y-cy) * 2/3 / size
    q = math.floor(q+.5) if q > 0 else math.ceil(q-.5)
    r = math.floor(r+.5) if r > 0 else math.ceil(r-.5)
    return Point(q, r)



main()