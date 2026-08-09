# Correção do Cálculo e Exibição de Feriados

O objetivo é garantir que os feriados sejam tratados exatamente como domingos na lógica de negócio e na interface, conforme solicitado pelo usuário, corrigindo a percepção de que o cálculo não funciona corretamente em certos cenários.

## User Review Required

> [!IMPORTANT]
> Para seguir a instrução de que o feriado deve ser "considerado como um domingo", os feriados marcados serão removidos da lista principal de lançamentos e do gráfico.
>
> **Decisão de UX:** Para permitir que o usuário desmarque um feriado caso tenha cometido um erro, adicionaremos uma seção opcional no final da lista chamada "Feriados e Domingos Ocultos" ou manteremos os feriados na lista com um estilo visual desabilitado. A proposta inicial é manter na lista mas com estilo "Sunday-like" para evitar que o dia suma e o usuário perca o controle.

## Proposed Changes

### [Component Name] UI Screens & Logic

#### [MODIFY] [SalesViewModel.kt](file:///C:/Users/Josué/Documents/App%20Erica/app/src/main/java/com/erica/metas/data/SalesViewModel.kt)
- Revisar `getRequiredDailyAverage` para garantir que a lógica de "falta de dias úteis" seja reativa e precisa.
- Adicionar um método `getWorkingDaysCount()` para centralizar o cálculo de dias úteis (excluindo domingos e feriados).

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Josué/Documents/App%20Erica/app/src/main/java/com/erica/metas/ui/screens/HomeScreen.kt)
- Ajustar a filtragem da `workingDaysList`.
- **Ajuste:** Em vez de remover completamente, vamos manter os feriados na lista para edição, mas garantir que o `DailySalesItem` reflita que aquele dia não conta para a meta diária.

#### [MODIFY] [ChartScreen.kt](file:///C:/Users/Josué/Documents/App%20Erica/app/src/main/java/com/erica/metas/ui/screens/ChartScreen.kt)
- Garantir que `workingDays` no gráfico EXCLUA feriados e domingos, para que a linha de meta e as barras reflitam apenas os dias de trabalho reais.

#### [MODIFY] [DailySalesItem.kt](file:///C:/Users/Josué/Documents/App%20Erica/app/src/main/java/com/erica/metas/ui/components/DailySalesItem.kt)
- Atualizar o estilo visual quando `isHoliday` for true para se parecer com um dia "fora da meta" ou desabilitado, reforçando que ele não é um dia útil.

## Verification Plan

### Automated Tests
- Executar os testes unitários criados pelo sub-agente em `SalesViewModelTest.kt` para garantir que a lógica matemática está correta para Meta, Super Meta e Vendas Zeradas.

### Manual Verification
1. Abrir o app com vendas zeradas.
2. Marcar um dia útil como feriado.
3. Verificar se a "venda diária" no topo aumenta.
4. Lançar vendas até bater a Meta.
5. Marcar outro dia como feriado.
6. Verificar se a "venda diária" (agora para Super Meta) aumenta.
7. Verificar se no Gráfico o dia marcado como feriado desapareceu (ou não possui barra e não conta para a média).
