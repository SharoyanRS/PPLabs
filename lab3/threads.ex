defmodule Integration do
  def calculate_integral(f, a, b, h, n, num_threads) do
    create_threads(f, a, b, h, n, 1, num_threads)
  |> Enum.map(&Task.await/1)
  |> Enum.sort(&Map.get(&1, :Время) <= Map.get(&2, :Время))
  end


  defp create_threads(_, _, _, _, _, current_thread, num_threads) when current_thread > num_threads, do: []
  defp create_threads(f, a, b, h, n, current_thread, num_threads) do

  task = Task.async(fn -> calculate_trapz_integral(f, a, b, h, n, current_thread) end)
  [task | create_threads(f, a, b, h, n, current_thread + 1, num_threads)]
  end

  defp calculate_trapz_integral(f, a, b, h, n, current_thread) do
  start_time = :os.system_time(:millisecond)
  result = trapz_integral(f, a, b, h, n)
  end_time = :os.system_time(:millisecond)
  elapsed_time = (end_time - start_time) / 1000
  %{Количество_потоков: current_thread, Значение_А: a, Значение_В: b, Шаг: h, Интеграл: result, Время: elapsed_time}
  end

  defp trapz_integral(f, a, b, h, n) do
  sum = h * ((f.(a) + f.(b)) / 2.0)
  Enum.reduce(1..(n - 1), sum, fn i, acc ->
  x = a + i * h
  acc + f.(x)
  end)
  |> Kernel.*(h)
  end
  end

  f = fn x -> :math.sin(x) end
  a = 0.0
  b = :math.pi
  h = 0.0001
  n = 100000
  
  Enum.each(1..20, fn num_threads ->
  maps = Integration.calculate_integral(f, a, b, h, n, num_threads)
 
  IO.puts("thread count: #{length(maps)}")

 
  time = Enum.reduce(maps,0, fn map, acc ->
    acc + Map.get(map, :Время)
    end)
  res = Enum.reduce(maps,0, fn map, acc -> Map.get(map, :Интеграл)
  end)
  IO.puts(time)
  IO.puts(res)
  IO.puts("----------------Я закончил считать!-----------------")
  end)