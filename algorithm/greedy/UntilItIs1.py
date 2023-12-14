# 1이 될 때까지
# N은 최대 100,000이다. O(N log N)의 알고리즘으로 접근한다.
# N, K를 공백으로 구분하여 입력받기
n, k = map(int, input().split())

result = 0

# N은 K보다 항상 크거나 같다. N이 K 이상이라면 K로 계속 나눈다.
while n >= k:
    # N이 K로 나누어 떨어지지 않는다면 N에서 1씩 빼기
    while n % k != 0:
        n -= 1
        result += 1

    # K로 나누기
    n //= k
    result += 1

# 마지막으로 남은 수에 대하여 1씩 빼기
while n > 1:
    n -= 1
    result += 1

print(result)