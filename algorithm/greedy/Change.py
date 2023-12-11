# 거스름돈
n = 1260
count = 0

coin_types = [500, 100, 50, 10]

for coin in coin_types:
    count += n // coin  # "//" 연산자는 몫을 의미한다.
    n %= coin

print(count)
