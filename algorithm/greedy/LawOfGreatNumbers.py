# 큰 수의 법칙
# N은 최대 1000이다. O(n^2)의 알고리즘으로 접근한다.
# N, M, K를 공백으로 구분하여 입력받기
n, m, k = map(int ,input().split())

# N개의 수를 공백으로 구분하여 입력받기
data = list(map(int, input().split()))

# 입력받은 수들 정렬하기
data.sort()

first = data[n - 1]
second = data[n - 2]

result = 0

while True:
    for i in range(k):  # 가장 큰 수를 K번 더하기
        if m == 0:
            break
        result += first
        m -= 1

    if m == 0:
        break

    result += second
    m -= 1

print(result)
