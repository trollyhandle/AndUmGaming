from graphics import *
import math


colors = ["black", "red", "blue", "green", "orange", "darkblue", "yellow"]

# WIN_SIZE = 800
WIN_SIZE = 1100


def main():

    win_center = Point(WIN_SIZE//2, WIN_SIZE//2)
    hex_size = 80
    # hex_size = 120
    hex_size_short = hex_size * math.sqrt(3) / 2

    done = Circle(Point(20, 20), 15)
    done.setOutline("red")

    board = Board()
    board.place(win_center, hex_size)

    print()
    print(board)

    win = GraphWin("prototype", WIN_SIZE, WIN_SIZE, autoflush=False)
    done.draw(win)
    board.draw(win)
    win.update()
    win.autoflush = True

    click = win.getMouse()
    while not in_shape(done, click):
        board.get_vertex_click(click)
        board.get_hex_click(click)
        click = win.getMouse()

    win.close()


class Board:
    def __init__(self, rings=2):
        self.rings = rings
        hexes = [[None for i in range(rings*2+1)] for i in range(rings*2+1)]
        # num = 0
        # for q in range(-rings, rings+1):
        #     for r in range(max(-rings,-q-rings), min(rings, -q+rings)+1):
        #         hx_coord = Point(q, r)
        #         print("placing H {:2} in {:2} {:2}".format(num, q, r), sep="")  # DEBUG
        #         if hexes[q][r] is not None: print("DUPLICATE")  # DEBUG
        #         hexes[q][r] = Hex(hx_coord)
        #         num += 1


        print('\n')  # DEBUG

        rings *= 2
        vertices = [[None for i in range(rings*2+1)] for i in range(rings*2+1)]
        num = 0
        for q in range(-rings, rings+1):
            for r in range(max(-rings,-q-rings), min(rings, -q+rings)+1):
                vx_coord = Point(q, r)
                # print("placing V {:2} in {:2} {:2}".format(num, vx_coord.x, vx_coord.y), sep="")  # DEBUG
                if vertices[q][r] is not None:
                    print("DUPLICATE")  # DEBUG
                vertices[q][r] = Vertex(vx_coord)
                if abs(q-r) % 3 == 0:  # if |q-r| is a multiple of 3
                    hx_coord = vx_coord  # CONVERT THIS PROPERLY
                    print("hex {:2} at {:2}, {:2}  ->  {:2}, {:2}".format(num, q, r, hx_coord.x, hx_coord.y))
                    if hexes[hx_coord.x][hx_coord.y] is not None: print("DUPLICATE", q, r)  # DEBUG
                    vertices[q][r] = Hex(vx_coord)
                    # hexes[hx_coord.x][hx_coord.y] = vertices[q][r]
                    # vertices[q][r] = None#Hex(hx_coord)

                    num += 1

        self.hexes = hexes
        self.vertices = vertices

        print("\n")  # DEBUG

    def place(self, center, hex_size):
        self.hex_size = hex_size
        self.center = center
        self.hex_size_short = hex_size * math.sqrt(3) / 2
        # self.vertex_size = (hex_size-hex_size_short)*2 # 3

        # for hex_row in self.hexes:
        #     for hex in hex_row:
        #         if hex is not None:
        #             hex.place(self.center, self.hex_size)
        for v_row in self.vertices:
            for v in v_row:
                if v is not None:
                    v.place(self.center, self.hex_size)

    def replace(self, win, center, hex_size):
        self.undraw()
        self.place(center, hex_size)
        self.draw(win)

    def draw(self, win):
        # for hex_row in self.hexes:
        #     for hex in hex_row:
        #         if hex is not None:
        #             # hex.setColor("light gray")
        #             hex.draw(win)
        for v_row in self.vertices:
            for v in v_row:
                if v is not None:
                    v.draw(win)
    def undraw(self):
        # for hex_row in self.hexes:
        #     for hex in hex_row:
        #         if hex is not None:
        #             # hex.setColor("light gray")
        #             hex.undraw()
        for v_row in self.vertices:
            for v in v_row:
                if v is not None:
                    v.undraw()

    def get_hex_click(self, click):
        for h_row in self.hexes:
            for h in h_row:
                if h is not None and h.is_click_in(click):
                    print('[',h.hx_coord.x, ', ', h.hx_coord.y, ']', sep="")  # DEBUG
                    h.setColor("red")
                    time.sleep(.35)
                    h.setColor("black")
        # where = pixel_to_hex(self.center, self.hex_size, click.getX(), click.getY())
        # print('(',where.x, ', ', where.y, ')', sep="", end=" ")  # DEBUG
        # print('(',click.x, ', ', click.y, ')', sep="")  # DEBUG
        # if self.hexes[where.x][where.y] is not None:
        #     self.hexes[where.x][where.y].setColor("red")
        #     time.sleep(.35)
        #     self.hexes[where.x][where.y].setColor()#"light gray")
    def get_vertex_click(self, click):
        for v_row in self.vertices:
            for v in v_row:
                if v is not None and v.is_click_in(click):
                    print('(',v.vx_coord.x, ', ', v.vx_coord.y, ')', sep="")  # DEBUG
                    v.setColor("red")
                    time.sleep(.35)
                    v.setColor("blue")
        # where = pixel_to_vertex(self.center, self.hex_size, click.getX(), click.getY())
        # print('(',where.x, ', ', where.y, ')', sep="", end=" ")  # DEBUG
        # print('(',click.x, ', ', click.y, ')', sep="")  # DEBUG
        # if self.vertices[where.x][where.y] is not None:
        #     self.vertices[where.x][where.y].setColor("red")
        #     time.sleep(.35)
        #     self.vertices[where.x][where.y].setColor()#"light gray")

    def __str__(self):
        prt = ""
        for r in range(-len(self.hexes)//2+1, len(self.hexes)//2+1):
            for q in range(-len(self.hexes[r])//2+1, len(self.hexes[r])//2+1):
                if self.hexes[r][q] is not None:
                    prt += self.hexes[r][q].__str__() + " "
                else:
                    prt += "   ..   "
            prt += '\n'
        prt += '\n\n'
        for r in range(-len(self.vertices)//2+1, len(self.vertices)//2+1):
            for q in range(-len(self.vertices[r])//2+1, len(self.vertices[r])//2+1):
                if self.vertices[r][q] is not None:
                    prt += self.vertices[r][q].__str__() + " "
                else:
                    prt += "   ..   "
            prt += '\n'
        return prt


class Hex:
    def __init__(self, hx_coord, resource=None):
        self.resource = resource
        self.hx_coord = hx_coord
        self.text = str(hx_coord.x) + ", " + str(hx_coord.y)
        self.center = None

    def __str__(self):
        return "[{:2},{:2}]".format(self.hx_coord.x, self.hx_coord.y)

    def place(self, grid_center, size, color="black"):
        self.size = size
        self.size_short = size * math.sqrt(3) / 2

        self.center = hex_to_pixel(grid_center, size, self.hx_coord.x, self.hx_coord.y)
        self.poly = Polygon(jump_linear(self.center, 0, size),
                            jump_linear(self.center, 60, size),
                            jump_linear(self.center, 120, size),
                            jump_linear(self.center, 180, size),
                            jump_linear(self.center, 240, size),
                            jump_linear(self.center, 300, size))
        self.cir = Circle(self.center, self.size_short)
        self.cir_big = Circle(self.center, self.size)
        self.txt = Text(Point(self.center.x, self.center.y), self.text)
        self.txt.setSize(max(size//5, 5))

    def draw(self, win):
        if self.center is not None:
            self.poly.draw(win)
            self.cir.draw(win)
            self.txt.draw(win)
            # self.cir_big.draw(win)
    def undraw(self):
        if self.center is not None:
            self.poly.undraw()
            self.cir.undraw()
            self.txt.undraw()
            # self.cir_big.draw(win)

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
    def __init__(self, vx_coord):
        self.vx_coord = vx_coord
        # self.text = str(vx_coord.x) + ", " + str(vx_coord.y)
        self.text = str(vx_coord.x-vx_coord.y)
        self.directions = [ (+1,  0), (+1, -1), ( 0, -1),
                            (-1,  0), (-1, +1), ( 0, +1)]

    def __str__(self):
        return "({:2},{:2})".format(self.vx_coord.x, self.vx_coord.y)

    def place(self, grid_center, size):
        self.size = size
        self.size_short = size * math.sqrt(3) / 2
        self.vertex_size = size//4
        self.vertex_size_short = self.vertex_size * math.sqrt(3) / 2

        self.center = vertex_to_pixel(grid_center, self.size_short*2//3, self.vx_coord.x, self.vx_coord.y)
        # self.center = vertex_to_pixel(grid_center, self.size_short, self.vx_coord.x, self.vx_coord.y)
        self.poly = Polygon(jump_linear(self.center, 30, self.vertex_size),
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
        self.txt.draw(win)
        # self.cir_center.draw(win)
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
        return Point(self.vx_coord.x + self.directions[dir][0],
                     self.vx_coord.y + self.directions[dir][1])



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

def jump_hex(point, size, r, q):
    # Calculates cartesian coordinates of destination,
    # given cartesian origin coordinates and a hexagonal travel vector
    x, y = point.getX(), point.getY()
    qr_x = r * size * 3 / 2
    qr_y = size * math.sqrt(3) * (q + r / 2)
    return Point(x + qr_x, y + qr_y)


def pixel_to_hex(center, size, x, y):
    # calculates axial coordinates, given x,y coordinates and coordinates
    # of the central hex
    cx, cy = center.getX(), center.getY()
    q = (x-cx) * 2/3 / size
    r = (-(x-cx) / 3 + math.sqrt(3)/3 * (y-cy)) / size
    q = math.floor(q+.5) if q > 0 else math.ceil(q-.5)
    r = math.floor(r+.5) if r > 0 else math.ceil(r-.5)
    return Point(q, r)

def pixel_to_vertex(center, size, x, y):
    # calculates axial coordinates, given x,y coordinates and coordinates
    # of the central hex
    cx, cy = center.getX(), center.getY()
    q = ((x-cx) * math.sqrt(3)/3 - (y-cy) / 3) / size
    r = (y-cy) * 2/3 / size
    q = math.floor(q+.5) if q > 0 else math.ceil(q-.5)
    r = math.floor(r+.5) if r > 0 else math.ceil(r-.5)
    return Point(q, r)

def hex_to_pixel(center, size, q, r):
    x = size * 3/2 * q
    y = size * math.sqrt(3) * (r + q/2)
    return Point(x + center.x, y + center.y)

def vertex_to_pixel(center, size, q, r):
    x = size * math.sqrt(3) * (q + r/2)
    y = size * 3/2 * r
    return Point(x + center.x, y + center.y)

def hex_round(q, r):
    # rounds towards 0
    # q = math.floor(q+.5) if q > 0 else math.ceil(q-.5)
    # r = math.floor(r+.5) if r > 0 else math.ceil(r-.5)
    return Point(int(q), int(r))

main()