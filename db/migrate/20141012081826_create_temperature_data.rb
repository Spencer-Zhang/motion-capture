class CreateTemperatureData < ActiveRecord::Migration
  def change
    create_table :temperature_data do |t|

      t.timestamps
    end
  end
end
