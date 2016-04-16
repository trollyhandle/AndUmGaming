__author__ = 'Tyler'

# Hex class, vertex checker
def is_click_near(self, point):
    x, y = point.getX(), point.getY()
    for i in range(6): # 6 vertices to check
        c = jump(self.center, i*60, self.size)
        cx, cy = c.getX(), c.getY()
        # if radius < distance
        if ((self.size-self.size_short)*3)**2 > (cx-x)**2 + (cy-y)**2:
            return True
    return False